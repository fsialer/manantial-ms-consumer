package com.fernando.manantial_ms_consumer.domain.services;

import com.fernando.manantial_ms_consumer.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.GenerateFilePdfPort;
import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
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
        doNothing().when(storeFilePort).store(anyString(),any(),anyString(),anyString());
        Customer customer  = TestUtilCustomer.buildCustomerMock();
        ReflectionTestUtils.setField(customerService, "customerInformationPath", "/pds");
        customerService.generatePdfCustomer(customer);
        Mockito.verify(generateFilePdfPort,times(1)).generatePdfCustomer(any(Customer.class));
        Mockito.verify(storeFilePort,times(1)).store(anyString(),any(),anyString(),anyString());
    }
}
