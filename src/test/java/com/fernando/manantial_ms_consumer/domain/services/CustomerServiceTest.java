package com.fernando.manantial_ms_consumer.domain.services;

import com.fernando.manantial_ms_consumer.application.ports.output.CustomerFilePersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.GenerateFilePdfPort;
import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerNotFoundException;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.utils.TestUtilCustomer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerPersistencePort customerPersistencePort;

    @Mock
    private GenerateFilePdfPort generateFilePdfPort;

    @Mock
    private StoreFilePort storeFilePort;

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerFilePersistencePort customerFilePersistencePort;

    @Test
    @DisplayName("When saving a customer, expect saved successfully")
    void When_SavingACustomer_ExpectSavedSuccessFully(){
        Customer customer = TestUtilCustomer.buildCustomerMock();
        when(customerPersistencePort.saveCustomer(any(Customer.class))).thenReturn(Mono.just(Boolean.TRUE));
        Mono<Boolean> result = customerService.save(customer);
        StepVerifier.create(result)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).saveCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("When saving a customer failed, Expected Not Saved")
    void When_SavingACustomerFailed_Expected_Not_Saved(){
        Customer customer = TestUtilCustomer.buildCustomerMock();
        when(customerPersistencePort.saveCustomer(any(Customer.class))).thenReturn(Mono.just(Boolean.FALSE));
        Mono<Boolean> result = customerService.save(customer);
        StepVerifier.create(result)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).saveCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("Expect A Exception When saving a customer failed")
    void Expect_AException_WhenSavingACustomerFailed(){
        Customer customer = TestUtilCustomer.buildCustomerMock();
        when(customerPersistencePort.saveCustomer(any(Customer.class))).thenReturn(Mono.error(new Exception()));
        Mono<Boolean> result = customerService.save(customer);
        StepVerifier.create(result)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).saveCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("When Generating Pdf For Customer Expect Pdf Generated And Stored")
    void When_GeneratingPdfForCustomer_Expect_PdfGeneratedAndStored(){
        when(generateFilePdfPort.generatePdfCustomer(any(Customer.class))).thenReturn(new byte[0]);
        when(customerFilePersistencePort.saveCustomerFile(any())).thenReturn(Mono.just(Boolean.TRUE));
        doNothing().when(storeFilePort).store(anyString(),any(),anyString(),anyString());
        Customer customer  = TestUtilCustomer.buildCustomerMock();
        ReflectionTestUtils.setField(customerService, "customerInformationPath", "/pds");
        customerService.generatePdfCustomer(customer);
        Mockito.verify(generateFilePdfPort,times(1)).generatePdfCustomer(any(Customer.class));
        Mockito.verify(storeFilePort,times(1)).store(anyString(),any(),anyString(),anyString());
        Mockito.verify(customerFilePersistencePort,times(1)).saveCustomerFile(any());
    }

    @Test
    @DisplayName("When Generating Pdf For Customer Expect Return False")
    void When_CusrtomerFileSaveFailed_Expect_ReturnFalse(){
        when(generateFilePdfPort.generatePdfCustomer(any(Customer.class))).thenReturn(new byte[0]);
        when(customerFilePersistencePort.saveCustomerFile(any())).thenReturn(Mono.just(Boolean.FALSE));
        Customer customer  = TestUtilCustomer.buildCustomerMock();
        ReflectionTestUtils.setField(customerService, "customerInformationPath", "/pds");
        customerService.generatePdfCustomer(customer);
        Mockito.verify(generateFilePdfPort,times(1)).generatePdfCustomer(any(Customer.class));
        Mockito.verify(storeFilePort,times(0)).store(anyString(),any(),anyString(),anyString());
        Mockito.verify(customerFilePersistencePort,times(1)).saveCustomerFile(any());
    }

    @Test
    @DisplayName("When Delete A Customer Correctly Expect Result True")
    void When_DeleteACustomerCorrectly_Expect_ResultTrue(){
        Customer customer=TestUtilCustomer.buildCustomerMock();
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.just(customer));
        when(customerPersistencePort.deleteCustomer(anyString())).thenReturn(Mono.just(Boolean.TRUE));
        Mono<Void> result = customerService.delete("14ds");
        StepVerifier.create(result).verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).deleteCustomer(anyString());
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
    }

    @Test
    @DisplayName("Expect CustomerNotFoundException When Key CustomerDo Not Exists")
    void Expect_CustomerNotFoundException_When_KeyCustomerDoNotExists(){
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.empty());
        Mono<Void> result = customerService.delete("14ds");
        StepVerifier.create(result).expectError(CustomerNotFoundException.class)
                .verify();
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
        Mockito.verify(customerPersistencePort,times(0)).deleteCustomer(anyString());
    }

    @Test
    @DisplayName("When Delete A Customer Failed Expect Result False")
    void When_DeleteACustomerCFailed_Expect_ResultFalse(){
        Customer customer=TestUtilCustomer.buildCustomerMock();
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.just(customer));
        when(customerPersistencePort.deleteCustomer(anyString())).thenReturn(Mono.just(Boolean.FALSE));
        Mono<Void> result = customerService.delete("14ds");
        StepVerifier.create(result).verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
        Mockito.verify(customerPersistencePort,times(1)).deleteCustomer(anyString());
    }

    @Test
    @DisplayName("Expect RuntimeException When Delete A Customer Failed")
    void Expect_RuntimeException_When_DeleteACustomerFailed(){
        Customer customer=TestUtilCustomer.buildCustomerMock();
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.just(customer));
        when(customerPersistencePort.deleteCustomer(anyString())).thenReturn(Mono.error(RuntimeException::new));
        Mono<Void> result = customerService.delete("14ds");
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
        Mockito.verify(customerPersistencePort,times(1)).deleteCustomer(anyString());
    }
}
