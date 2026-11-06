package org.example.services;

import org.example.config.S3config;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

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
}