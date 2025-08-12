package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade;

import org.springframework.stereotype.Component;

@Component
public class AzureBlobStoreStorageDrive implements StorageDrive{
    @Override
    public void uploadFile(String fileName, byte[] content,String path, String contentType) {

    }
}
