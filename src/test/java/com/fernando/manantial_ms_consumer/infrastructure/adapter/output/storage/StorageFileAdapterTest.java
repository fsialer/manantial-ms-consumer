package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage;

import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade.*;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageFileAdapterTest {
    @InjectMocks
    private StorageFileAdapter storageFileAdapter;

    @Mock
    private StorageDrive mockDrive;

    @Mock
    private StorageFactory storageFactory;


    @ParameterizedTest
    @CsvSource({
            "local, /pdfs",
            "s3, ignored",
            "blob, ignored"
    })
    @DisplayName("When storing a file, expect uploadFile to be called with correct parameters")
    void When_StoringAFile_ExpectUploadFileToBeCalledWithCorrectParameters(String storageType, String path) {
        String fileName = storageType+"test.pdf";
        byte[] content = "Hello World".getBytes();
        when(storageFactory.getStorageDrive(storageType)).thenReturn(mockDrive);
        String contenType = "application/pdf";

        ReflectionTestUtils.setField(storageFileAdapter, "storageType", storageType);
        doNothing().when(mockDrive).uploadFile(anyString(),any(),anyString(),anyString());
        storageFileAdapter.store(fileName, content,path,contenType);
        verify(mockDrive,times(1)).uploadFile(anyString(), any(),anyString(),anyString());
    }

    @Test
    @DisplayName("Expect IllegalArgumentException for storage type unsupported When storing a file")
    void Expect_IllegalArgumentExceptionForStorageTypeUnsupported_When_StoringAFile(){
        String unsupportedStorageType = "unsupported";
        String path = "/pdfs";
        String contentType = "application/pdf";
        ReflectionTestUtils.setField(storageFileAdapter, "storageType", unsupportedStorageType);
        when(storageFactory.getStorageDrive(unsupportedStorageType)).thenThrow(new IllegalArgumentException("Unsupported storage type: ".concat(unsupportedStorageType)));
        assertThrows(IllegalArgumentException.class,()->storageFileAdapter.store("file.pdf","content".getBytes(),path,contentType));
        verify(mockDrive,times(0)).uploadFile(anyString(), any(),anyString(),anyString());
    }
}
