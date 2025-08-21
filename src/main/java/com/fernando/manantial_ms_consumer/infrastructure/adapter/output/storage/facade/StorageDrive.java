package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade;

public interface StorageDrive {
    void uploadFile(String fileName, byte[] content, String path, String contentType);
    void deleteFile(String path);
    byte[] getFile(String path);
}
