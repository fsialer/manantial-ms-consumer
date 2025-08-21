package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage;

import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @ParameterizedTest
    @CsvSource({
            "local, /pdfs",
            "s3, ignored",
            "blob, ignored"
    })
    @DisplayName("When Delete Customer Expect Delete File Customer")
    void When_DeleteCustomer_Expect_DeleteFileCustomer(String storageType, String path) {
        String fileName = storageType+"test.pdf";
        when(storageFactory.getStorageDrive(storageType)).thenReturn(mockDrive);
        ReflectionTestUtils.setField(storageFileAdapter, "storageType", storageType);
        doNothing().when(mockDrive).deleteFile(anyString());
        storageFileAdapter.delete(path.concat("/").concat(fileName));
        verify(mockDrive,times(1)).deleteFile(anyString());
    }

    @Test
    @DisplayName("Expect IllegalArgumentException for storage type unsupported When delete a file")
    void Expect_IllegalArgumentExceptionForStorageTypeUnsupported_When_DeleteAFile() {
        String unsupportedStorageType = "unsupported";
        String path = "/pdfs/file.pdf";
        ReflectionTestUtils.setField(storageFileAdapter, "storageType", unsupportedStorageType);
        when(storageFactory.getStorageDrive(unsupportedStorageType)).thenThrow(new IllegalArgumentException("Unsupported storage type: ".concat(unsupportedStorageType)));
        assertThrows(IllegalArgumentException.class,()->storageFileAdapter.delete(path));
        verify(mockDrive,times(0)).deleteFile(anyString());
    }

    @ParameterizedTest
    @CsvSource({
            "local, pdfs/file.pdf",
            "s3, ignored",
            "blob, ignored"
    })
    @DisplayName("When PathFile Is Correct Expect Found File ByteArrays")
    void When_PathFileIsCorrect_Expect_FoundFileByteArrays(String storageType, String path) {
        byte[] expectedBytes = "Hello S3".getBytes();
        ReflectionTestUtils.setField(storageFileAdapter, "storageType", storageType);
        when(storageFactory.getStorageDrive(storageType)).thenReturn(mockDrive);
        when(mockDrive.getFile(path)).thenReturn(expectedBytes);
        byte[] files= storageFileAdapter.getFile(path);
        assertEquals(expectedBytes,files);
        verify(mockDrive,times(1)).getFile(anyString());
    }
}
