package org.example.services;

import org.example.config.S3config;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.InputStream;
import java.util.*;

public class s3service {

    public void uploadAFile(
            String BucketName,
            String fileName,
            InputStream inputStream,
            long fileSize) {

        try (S3Client s3 = S3config.getS3Client()) {

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BucketName)
                    .key(fileName)
                    .build();

            s3.putObject(
                    request,
                    RequestBody.fromInputStream(inputStream, fileSize)
            );

            System.out.println("File uploaded successfully!");

        } catch (Exception e) {
            throw new RuntimeException("S3 upload failed", e);
        }
    }
    public List<S3Object> getAllObjects(String bucketName) {

        List<S3Object> objects = new ArrayList<>();

        ListObjectsV2Request request =
                ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .build();

        try(S3Client s3 = S3config.getS3Client()){
            ListObjectsV2Iterable result =
                    s3.listObjectsV2Paginator(request);
            for (S3Object object : result.contents()) {
                objects.add(object);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




        return objects;
    }

    // Step 2: Get information about selected object
    public HeadObjectResponse getObjectInformation(
            String bucketName, String objectKey) {
        try(S3Client s3 = S3config.getS3Client()){
            HeadObjectRequest request =
                    HeadObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .build();

            return s3.headObject(request);

        } catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException(e);
        }
    }
}