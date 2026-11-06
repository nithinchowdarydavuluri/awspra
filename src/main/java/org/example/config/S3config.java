package org.example.config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3config {

    public static S3Client getS3Client() {

        return S3Client.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}