package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StorageFactoryTest {
    @InjectMocks
    private StorageFactory storageFactory;
    @Mock
    private  AwsS3StorageDrive awsS3StorageDrive;
    @Mock
    private  LocalStorageDrive localStorageDrive;
    @Mock
    private  AzureBlobStoreStorageDrive azureBlobStoreStorageDrive;

    @Test
    @DisplayName("When Type Drive Is Local Expect Instance LocalStorageDrive")
    void When_TypeDriveIsLocal_Expect_Expect_InstanceLocalStorageDrive() {
        StorageDrive drive = storageFactory.getStorageDrive("local");
        assertInstanceOf(LocalStorageDrive.class, drive);
    }

    @Test
    @DisplayName("When Type Drive Is S3 Expect Instance AwsS3StorageDrive")
    void When_TypeDriveIsS3_Expect_InstanceAwsS3StorageDrive() {
        StorageDrive drive = storageFactory.getStorageDrive("s3");
        assertInstanceOf(AwsS3StorageDrive.class, drive);
    }

    @Test
    @DisplayName("When Type Drive Is Blob Expect Instance AzureBlobStoreStorageDrive")
    void When_TypeDriveIsBlob_Expect_InstanceAzureBlobStoreStorageDrive() {
        StorageDrive drive = storageFactory.getStorageDrive("blob");
        assertInstanceOf(AzureBlobStoreStorageDrive.class, drive);
    }

    @Test
    @DisplayName("Expect IllegalArgumentException When Type Drive Is Unsupported")
    void Expect_IllegalArgumentException_When_TypeDriveIsUnsupported() {
        assertThrows(IllegalArgumentException.class, () -> {
            storageFactory.getStorageDrive("invalid");
        });
    }
}
