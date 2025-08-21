package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class AwsS3StorageDrive implements StorageDrive{

    @Value("${amazon.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public void uploadFile(String fileName, byte[] content, String path, String contentType) {
        ByteArrayInputStream inputStream=new ByteArrayInputStream(content);
        ObjectMetadata metadata=new ObjectMetadata();
        metadata.setContentLength(content.length);
        metadata.setContentType(contentType);
        amazonS3.putObject(bucket, path.concat("/").concat(fileName),inputStream,metadata);
        log.info("✅ file upload  successfully to aws s3");
    }

    @Override
    public void deleteFile(String path) {
        try {
            amazonS3.deleteObject(bucket, path);
            log.info("🗑️ File delete correctly in AWS S3: {}", path);
        } catch (Exception e) {
            log.error("❌ Error delete file in AWS S3: {}", path, e);
            throw new RuntimeException("Error deleting file from AWS S3", e);
        }
    }

    @Override
    public byte[] getFile(String path) {
        try {
            var s3Object = amazonS3.getObject(bucket, path);
            var inputStream = s3Object.getObjectContent();
            return inputStream.readAllBytes();
        } catch (Exception e) {
            log.error("❌ Error downloading file from AWS S3: {}", path, e);
            throw new RuntimeException("Error downloading file from AWS S3", e);
        }
    }
}
