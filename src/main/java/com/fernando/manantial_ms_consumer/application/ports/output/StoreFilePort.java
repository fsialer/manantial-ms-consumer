package com.fernando.manantial_ms_consumer.application.ports.output;

public interface StoreFilePort {
    void store(String fileName, byte[] content, String path, String contentType);
    void delete(String path);
}
