package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence;

import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.CustomerFilePersistenceMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerFileTemplate;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.CustomerFileRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerFilePersistenceAdapterTest {
    @Mock
    private CustomerFileRepository customerFileRepository;

    @Mock
    private CustomerFilePersistenceMapper customerFilePersistenceMapper;

    @InjectMocks
    private CustomerFilePersistenceAdapter customerFilePersistenceAdapter;

    @Test
    @DisplayName("When Saving CustomerFile Expect Saved Successfully")
    void When_SavingCustomerFile_ExpectSavedSuccessfully(){
        CustomerFile customer= TestUtilCustomerFile.buildCustomerFileMock();
        CustomerFileTemplate customerFileTemplate= TestUtilCustomerFile.buildCustomerFileTemplateMock();
        when(customerFileRepository.save(any(CustomerFileTemplate.class))).thenReturn(Mono.just(Boolean.TRUE));
        when(customerFilePersistenceMapper.customerFileToCustomerFileTemplate(any())).thenReturn(customerFileTemplate);
        Mono<Boolean> result = customerFilePersistenceAdapter.saveCustomerFile(customer);
        StepVerifier.create(result)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
        Mockito.verify(customerFileRepository,times(1)).save(any(CustomerFileTemplate.class));
        Mockito.verify(customerFilePersistenceMapper,times(1)).customerFileToCustomerFileTemplate(any());
    }

    @Test
    @DisplayName("When Saving CustomerFile Expect Saved Successfully")
    void When_SavingCustomerFileFailed_ExpectReturnFalse(){
        CustomerFile customer= TestUtilCustomerFile.buildCustomerFileMock();
        CustomerFileTemplate customerFileTemplate= TestUtilCustomerFile.buildCustomerFileTemplateMock();
        when(customerFileRepository.save(any(CustomerFileTemplate.class))).thenReturn(Mono.just(Boolean.FALSE));
        when(customerFilePersistenceMapper.customerFileToCustomerFileTemplate(any())).thenReturn(customerFileTemplate);
        Mono<Boolean> result = customerFilePersistenceAdapter.saveCustomerFile(customer);
        StepVerifier.create(result)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        Mockito.verify(customerFileRepository,times(1)).save(any(CustomerFileTemplate.class));
        Mockito.verify(customerFilePersistenceMapper,times(1)).customerFileToCustomerFileTemplate(any());
    }

    @Test
    @DisplayName("When Delete Customer Expect Deleted Successfully")
    void When_DeleteCustomer_ExpectDeletedSuccessfully(){
        when(customerFileRepository.delete(anyString())).thenReturn(Mono.just(Boolean.TRUE));
        Mono<Boolean> result = customerFilePersistenceAdapter.deleteCustomerFile("14sds");
        StepVerifier.create(result)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
        Mockito.verify(customerFileRepository,times(1)).delete(anyString());
    }

    @Test
    @DisplayName("When Delete Customer Failed Expect Do Not Deleted")
    void When_SavingCustomer_ExpectDoNotDeleted(){
        when(customerFileRepository.delete(anyString())).thenReturn(Mono.just(Boolean.FALSE));
        Mono<Boolean> result = customerFilePersistenceAdapter.deleteCustomerFile("14sds");
        StepVerifier.create(result)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        Mockito.verify(customerFileRepository,times(1)).delete(anyString());
    }

    @Test
    @DisplayName("When CustomerFile Id Exists Expect CustomerFile Information")
    void When_CustomerIdExists_Expect_CustomerFileInformation(){
        CustomerFileTemplate customerFileTemplate= TestUtilCustomerFile.buildCustomerFileTemplateMock();
        when(customerFileRepository.getCustomerFile(anyString())).thenReturn(Mono.just(customerFileTemplate));
        when(customerFilePersistenceMapper.customerFileTemplateToCustomerFile(any(CustomerFileTemplate.class))).thenReturn(TestUtilCustomerFile.buildCustomerFileMock());
        Mono<CustomerFile> result = customerFilePersistenceAdapter.getCustomerFile("12345");
        StepVerifier.create(result)
                .expectNextMatches(customerMatch->customerMatch.getId().equals(customerFileTemplate.getId())
                            && customerMatch.getPath().equals(customerFileTemplate.getPath())
                ).verifyComplete();
        Mockito.verify(customerFileRepository,times(1)).getCustomerFile(anyString());
        Mockito.verify(customerFilePersistenceMapper,times(1)).customerFileTemplateToCustomerFile(any());
    }
}
