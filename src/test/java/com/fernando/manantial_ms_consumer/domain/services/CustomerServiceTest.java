package com.fernando.manantial_ms_consumer.domain.services;

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

    @Test
    @DisplayName("When Generating Pdf For Customer Expect Pdf Generated And Stored")
    void When_GeneratingPdfForCustomer_Expect_PdfGeneratedAndStored(){
        Customer customer  = TestUtilCustomer.buildCustomerMock();
        when(generateFilePdfPort.generatePdfCustomer(any(Customer.class))).thenReturn(new byte[0]);
        when(customerPersistencePort.saveCustomer(any())).thenReturn(Mono.just(customer));
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.just(customer));
        doNothing().when(storeFilePort).store(anyString(),any(),anyString(),anyString());
        ReflectionTestUtils.setField(customerService, "customerInformationPath", "/pds");
        Mono<Void> result= customerService.generatePdfCustomer(customer);
        StepVerifier.create(result).verifyComplete();
        Mockito.verify(generateFilePdfPort,times(1)).generatePdfCustomer(any(Customer.class));
        Mockito.verify(storeFilePort,times(1)).store(anyString(),any(),anyString(),anyString());
        Mockito.verify(customerPersistencePort,times(1)).saveCustomer(any());
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
    }

    @Test
    @DisplayName("Expect CustomerNotFound When It Want Generate Pdf For Customer That Not Exists")
    void Expect_CustomerNotFound_WhenitWantGeneratePdfForCustomerThatNotExists(){
        Customer customer  = TestUtilCustomer.buildCustomerMock();
        when(generateFilePdfPort.generatePdfCustomer(any(Customer.class))).thenReturn(new byte[0]);
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.empty());
        ReflectionTestUtils.setField(customerService, "customerInformationPath", "/pds");
        Mono<Void> result=customerService.generatePdfCustomer(customer);
        StepVerifier.create(result).expectError(CustomerNotFoundException.class)
                .verify();
        Mockito.verify(generateFilePdfPort,times(1)).generatePdfCustomer(any(Customer.class));
        Mockito.verify(storeFilePort,times(0)).store(anyString(),any(),anyString(),anyString());
        Mockito.verify(customerPersistencePort,times(0)).saveCustomer(any());
    }

    @Test
    @DisplayName("When Delete A Customer Correctly Expect Result True")
    void When_DeleteACustomerCorrectly_Expect_ResultTrue(){
        doNothing().when(storeFilePort).delete(anyString());
        Mono<Void> result = customerService.delete("path/customer.pdf");
        StepVerifier.create(result).verifyComplete();
    }
}
