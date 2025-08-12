package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageFactory {
    private final AwsS3StorageDrive awsS3StorageDrive;
    private final LocalStorageDrive localStorageDrive;
    private final AzureBlobStoreStorageDrive azureBlobStoreStorageDrive;

    public StorageDrive getStorageDrive(String type){

        return switch( type.toLowerCase()){
            case "local"->localStorageDrive;
            case "s3"-> awsS3StorageDrive;
            case "blob"-> azureBlobStoreStorageDrive;
            default -> throw new IllegalArgumentException("Unsupported storage type: "+type);
        };
    }
}
