package com.fernando.manantial_ms_consumer.domain.services;

import com.fernando.manantial_ms_consumer.application.ports.output.CustomerFilePersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerFileNotFoundException;
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
}
