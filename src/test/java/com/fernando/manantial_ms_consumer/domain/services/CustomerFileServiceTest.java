package com.fernando.manantial_ms_consumer.domain.services;

import com.fernando.manantial_ms_consumer.application.ports.input.GetCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerFilePersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerFileNotFoundException;
import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerNotFoundException;
import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import com.fernando.manantial_ms_consumer.utils.TestUtilCustomerFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
@ExtendWith(MockitoExtension.class)
class CustomerFileServiceTest {
    @Mock
    private  CustomerFilePersistencePort customerFilePersistencePort;
    @Mock
    private  StoreFilePort storeFilePort;
    @InjectMocks
    private CustomerFileService customerFileService;


    @Test
    @DisplayName("When Delete A CustomerFile Correctly Expect Result True")
    void When_DeleteACustomerFileCorrectly_Expect_ResultTrue(){
        CustomerFile customerFile= TestUtilCustomerFile.buildCustomerFileMock();
        when(customerFilePersistencePort.getCustomerFile(anyString())).thenReturn(Mono.just(customerFile));
        when(customerFilePersistencePort.deleteCustomerFile(anyString())).thenReturn(Mono.just(Boolean.TRUE));
        doNothing().when(storeFilePort).delete(anyString());
        Mono<Void> result = customerFileService.delete("14ds");
        StepVerifier.create(result).verifyComplete();
        Mockito.verify(customerFilePersistencePort,times(1)).getCustomerFile(anyString());
        Mockito.verify(customerFilePersistencePort,times(1)).deleteCustomerFile(anyString());
        Mockito.verify(storeFilePort,times(1)).delete(anyString());
    }

    @Test
    @DisplayName("When Delete A CustomerFile Correctly Expect Result False")
    void When_DeleteACustomerFileCorrectly_Expect_ResultFalse(){
        CustomerFile customerFile= TestUtilCustomerFile.buildCustomerFileMock();
        when(customerFilePersistencePort.getCustomerFile(anyString())).thenReturn(Mono.just(customerFile));
        when(customerFilePersistencePort.deleteCustomerFile(anyString())).thenReturn(Mono.just(Boolean.FALSE));

        Mono<Void> result = customerFileService.delete("14ds");
        StepVerifier.create(result).verifyComplete();
        Mockito.verify(customerFilePersistencePort,times(1)).getCustomerFile(anyString());
        Mockito.verify(customerFilePersistencePort,times(1)).deleteCustomerFile(anyString());
        Mockito.verify(storeFilePort,times(0)).delete(anyString());
    }

    @Test
    @DisplayName("Expect CustomerFileNotFoundException When CustomerFile Do Not Exists")
    void Expect_CustomerFileNotFoundException_When_CustomerFileDoNotExists(){
        CustomerFile customerFile= TestUtilCustomerFile.buildCustomerFileMock();
        when(customerFilePersistencePort.getCustomerFile(anyString())).thenReturn(Mono.empty());
        Mono<Void> result = customerFileService.delete("14ds");
        StepVerifier.create(result).expectError(CustomerFileNotFoundException.class)
                .verify();
        Mockito.verify(customerFilePersistencePort,times(1)).getCustomerFile(anyString());
        Mockito.verify(customerFilePersistencePort,times(0)).deleteCustomerFile(anyString());
        Mockito.verify(storeFilePort,times(0)).delete(anyString());
    }

    @Test
    @DisplayName("Expect RuntimeException When CustomerFile Do Not Exists")
    void Expect_RuntimeException_When_CustomerFileDoNotExists(){
        CustomerFile customerFile= TestUtilCustomerFile.buildCustomerFileMock();
        when(customerFilePersistencePort.getCustomerFile(anyString())).thenReturn(Mono.just(customerFile));
        when(customerFilePersistencePort.deleteCustomerFile(anyString())).thenReturn(Mono.error(RuntimeException::new));
        Mono<Void> result = customerFileService.delete("14ds");
        StepVerifier.create(result).expectError(RuntimeException.class)
                .verify();
        Mockito.verify(customerFilePersistencePort,times(1)).getCustomerFile(anyString());
        Mockito.verify(customerFilePersistencePort,times(1)).deleteCustomerFile(anyString());
        Mockito.verify(storeFilePort,times(0)).delete(anyString());
    }

    @Test
    @DisplayName("When Path File Is Correct Expect An Array Bytes")
    void When_PathFileIsCorrect_Expect_AnArrayBytes() {
        // Arrange
        String path = "some/path/file.pdf";
        byte[] expectedBytes = "Hello S3".getBytes();
        when(storeFilePort.getFile(anyString())).thenReturn(expectedBytes);
        byte[] file=customerFileService.getFile(path);

        assertArrayEquals(expectedBytes, file);
        Mockito.verify(storeFilePort, times(1)).getFile(path);
    }

    @Test
    @DisplayName("When CustomerFile Id Exist Expect Information CustomerFile")
    void When_CustomerFileIdExist_Expect_InformationCustomerFile(){
        CustomerFile customerFile=TestUtilCustomerFile.buildCustomerFileMock();
        when(customerFilePersistencePort.getCustomerFile(anyString())).thenReturn(Mono.just(customerFile));
        Mono<CustomerFile> customerFileMono=customerFileService.getCustomerFile("sdsd545d1sd1sJohn");
        StepVerifier.create(customerFileMono)
                .expectNextMatches(customerMatch->{
                    return customerMatch.getId().equals(customerFile.getId())
                            && customerMatch.getPath().equals(customerFile.getPath())
                            && customerMatch.getFileName().equals(customerFile.getFileName())
                            && customerMatch.getContentType().equals(customerFile.getContentType());
                });
        Mockito.verify(customerFilePersistencePort,times(1)).getCustomerFile(anyString());
    }

    @Test
    @DisplayName("Expect Information CustomerFile Do Not Found When CustomerFileID Do Not Exists")
    void Expect_InformationCustomerFileDoNotFound_When_CustomerFileIDDoNotExists(){
        when(customerFilePersistencePort.getCustomerFile(anyString())).thenReturn(Mono.empty());
        Mono<CustomerFile> customerFileMono=customerFileService.getCustomerFile("sdsd545d1sd1sJohn");
        StepVerifier.create(customerFileMono)
                .expectError(CustomerNotFoundException.class);
        Mockito.verify(customerFilePersistencePort,times(1)).getCustomerFile(anyString());
    }
}
