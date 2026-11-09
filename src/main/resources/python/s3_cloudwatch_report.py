
import boto3
import csv
import sys
from datetime import datetime, timedelta, timezone


def generate_s3_report(bucket_name, region):

    cloudwatch = boto3.client(
        "cloudwatch",
        region_name=region
    )

    end_time = datetime.now(timezone.utc)
    start_time = end_time - timedelta(days=14)

    # Get the available storage-type metrics for this bucket
    paginator = cloudwatch.get_paginator("list_metrics")

    total_size_bytes = 0
    latest_metric_time = None
    storage_details = []

    for page in paginator.paginate(
            Namespace="AWS/S3",
            MetricName="BucketSizeBytes",
            Dimensions=[
                {"Name": "BucketName", "Value": bucket_name}
            ]
    ):
        for metric in page["Metrics"]:

            dimensions = {
                item["Name"]: item["Value"]
                for item in metric["Dimensions"]
            }

            storage_type = dimensions.get("StorageType")

            if not storage_type:
                continue

            result = cloudwatch.get_metric_statistics(
                Namespace="AWS/S3",
                MetricName="BucketSizeBytes",
                Dimensions=[
                    {"Name": "BucketName", "Value": bucket_name},
                    {"Name": "StorageType", "Value": storage_type}
                ],
                StartTime=start_time,
                EndTime=end_time,
                Period=86400,
                Statistics=["Average"]
            )

            datapoints = result["Datapoints"]

            if datapoints:
                latest = max(datapoints, key=lambda x: x["Timestamp"])

                size_bytes = latest["Average"]
                total_size_bytes += size_bytes

                storage_details.append({
                    "storage_type": storage_type,
                    "size_bytes": size_bytes
                })

                if latest_metric_time is None or latest["Timestamp"] > latest_metric_time:
                    latest_metric_time = latest["Timestamp"]

    # Get total number of objects
    object_result = cloudwatch.get_metric_statistics(
        Namespace="AWS/S3",
        MetricName="NumberOfObjects",
        Dimensions=[
            {"Name": "BucketName", "Value": bucket_name},
            {"Name": "StorageType", "Value": "AllStorageTypes"}
        ],
        StartTime=start_time,
        EndTime=end_time,
        Period=86400,
        Statistics=["Average"]
    )

    object_datapoints = object_result["Datapoints"]

    if object_datapoints:
        latest_objects = max(
            object_datapoints,
            key=lambda x: x["Timestamp"]
        )
        object_count = int(latest_objects["Average"])
        object_timestamp = latest_objects["Timestamp"]
    else:
        object_count = None
        object_timestamp = None

    total_size_gb = total_size_bytes / (1024 ** 3)

    # Display report
    print("\n========== S3 BUCKET REPORT ==========")
    print("Bucket Name       :", bucket_name)
    print("Total Size (Bytes):", round(total_size_bytes, 2))
    print("Total Size (GB)   :", round(total_size_gb, 4))
    print("Number of Objects :", object_count)
    print("Storage Metrics   :", latest_metric_time)
    print("Object Metrics    :", object_timestamp)

    # Save report to CSV
    filename = f"s3_report_{bucket_name}.csv"

    with open(filename, "w", newline="") as file:
        writer = csv.writer(file)

        writer.writerow(["Bucket Name", bucket_name])
        writer.writerow(["Total Size Bytes", total_size_bytes])
        writer.writerow(["Total Size GB", total_size_gb])
        writer.writerow(["Number of Objects", object_count])
        writer.writerow(["Storage Metrics Date", latest_metric_time])
        writer.writerow(["Object Metrics Date", object_timestamp])

        writer.writerow([])
        writer.writerow(["Storage Type", "Size Bytes"])

        for item in storage_details:
            writer.writerow([
                item["storage_type"],
                item["size_bytes"]
            ])

    print("\nReport generated:", filename)


if __name__ == "__main__":

    bucket = sys.argv[1]
    region = sys.argv[2]

    generate_s3_report(bucket, region)
