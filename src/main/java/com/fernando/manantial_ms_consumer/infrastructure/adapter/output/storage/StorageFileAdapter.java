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

    @Value("${storage.path}")
    private String path;

    @Override
    public void store(String fileName, byte[] content) {
        StorageFactory.getStorageDrive(storageType).uploadFile(fileName,content, path);
    }
}
