package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageFactory {

    public static StorageDrive getStorageDrive(String type){

        return switch( type.toLowerCase()){
            case "local"-> new LocalStorageDrive();
            case "s3"-> new AwsS3StorageDrive();
            case "blob"-> new AzureBlobStoreStorageDrive();
            default -> throw new IllegalArgumentException("Unsupported storage type: "+type);
        };
    }
}
