package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage;

import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade.StorageDrive;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade.StorageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StorageFileAdapterTest {
    @InjectMocks
    private StorageFileAdapter storageFileAdapter;

    @Mock
    private StorageDrive mockDrive;

    @ParameterizedTest
    //@ValueSource(strings = {"s3", "local", "elk"},strings={"/pdfs"})
    @CsvSource({
            "local, /pdfs",
            "s3, ignored",
            "blob, ignored"
    })
    @DisplayName("When storing a file, expect uploadFile to be called with correct parameters")
    void When_StoringAFile_ExpectUploadFileToBeCalledWithCorrectParameters(String storageType, String path) {
        String fileName = storageType+"test.pdf";
        byte[] content = "Hello World".getBytes();

        ReflectionTestUtils.setField(storageFileAdapter, "storageType", storageType);
        ReflectionTestUtils.setField(storageFileAdapter, "path", path);

        try (MockedStatic<StorageFactory> mockedFactory = Mockito.mockStatic(StorageFactory.class)) {
            mockedFactory.when(() -> StorageFactory.getStorageDrive(storageType))
                    .thenReturn(mockDrive);
            storageFileAdapter.store(fileName, content);
            verify(mockDrive,times(1)).uploadFile(anyString(), any(),anyString());
        }
    }

    @Test
    @DisplayName("Expect IllegalArgumentException for storage type unsupported When storing a file")
    void Expect_IllegalArgumentExceptionForStorageTypeUnsupported_When_StoringAFile(){
        String unsupportedStorageType = "unsupported";
        String path = "/pdfs";
        ReflectionTestUtils.setField(storageFileAdapter, "storageType", unsupportedStorageType);
        ReflectionTestUtils.setField(storageFileAdapter, "path", path);
        try(MockedStatic<StorageFactory> mockedFactory = Mockito.mockStatic(StorageFactory.class)){
            mockedFactory.when(()-> StorageFactory.getStorageDrive(unsupportedStorageType))
                    .thenThrow(new IllegalArgumentException("Unsupported storage type: "+unsupportedStorageType));
                    storageFileAdapter.store("file.pdf","content".getBytes());
            verify(mockDrive,times(0)).uploadFile(anyString(), any(),anyString());
        }catch(IllegalArgumentException e){
            assert(e.getMessage().equals("Unsupported storage type: "+unsupportedStorageType));
        }
    }
}
