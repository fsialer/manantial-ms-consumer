package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage;

import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade.StorageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageFileAdapter implements StoreFilePort {

    @Value("${storage.type}")
    private String storageType;
    private final StorageFactory storageFactory;

    @Override
    public void store(String fileName, byte[] content, String path, String contentType) {
        storageFactory.getStorageDrive(storageType).uploadFile(fileName,content, path, contentType);
    }

    @Override
    public void delete(String path) {
        storageFactory.getStorageDrive(storageType).deleteFile(path);
    }

    @Override
    public byte[] getFile(String path) {
        return storageFactory.getStorageDrive(storageType).getFile(path);
    }
}
