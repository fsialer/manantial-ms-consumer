package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerTemplate;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.CustomerRedisRepository;
import com.fernando.manantial_ms_consumer.utils.TestUtilCustomer;
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
class CustomerPersistenceAdapterTest {
    @Mock
    private CustomerRedisRepository customerRepository;

    @Mock
    private CustomerPersistenceMapper customerPersistenceMapper;

    @InjectMocks
    private CustomerPersistenceAdapter customerPersistenceAdapter;

    @Test
    @DisplayName("When Saving Customer Expect Saved Successfully")
    void When_SavingCustomer_ExpectSavedSuccessfully(){
        Customer customer= TestUtilCustomer.buildCustomerMock();
        CustomerTemplate customerTemplate= TestUtilCustomer.buildCustomerTemplateMock();
        when(customerRepository.save(any(CustomerTemplate.class))).thenReturn(Mono.just(Boolean.TRUE));
        when(customerPersistenceMapper.customerToCustomerTemplate(any(Customer.class))).thenReturn(customerTemplate);
        Mono<Boolean> result = customerPersistenceAdapter.saveCustomer(customer);
        StepVerifier.create(result)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
        Mockito.verify(customerRepository,times(1)).save(any(CustomerTemplate.class));
        Mockito.verify(customerPersistenceMapper,times(1)).customerToCustomerTemplate(any(Customer.class));
    }

    @Test
    @DisplayName("When Saving Customer Failed Expect Do Not Saved Successfully")
    void When_SavingCustomer_ExpectDoNotSavedSuccessfully(){
        Customer customer= TestUtilCustomer.buildCustomerMock();
        CustomerTemplate customerTemplate= TestUtilCustomer.buildCustomerTemplateMock();
        when(customerRepository.save(any(CustomerTemplate.class))).thenReturn(Mono.just(Boolean.FALSE));
        when(customerPersistenceMapper.customerToCustomerTemplate(any(Customer.class))).thenReturn(customerTemplate);
        Mono<Boolean> result = customerPersistenceAdapter.saveCustomer(customer);
        StepVerifier.create(result)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        Mockito.verify(customerRepository,times(1)).save(any(CustomerTemplate.class));
        Mockito.verify(customerPersistenceMapper,times(1)).customerToCustomerTemplate(any(Customer.class));
    }

    @Test
    @DisplayName("When Delete Customer Expect Deleted Successfully")
    void When_DeleteCustomer_ExpectDeletedSuccessfully(){
        when(customerRepository.delete(anyString())).thenReturn(Mono.just(Boolean.TRUE));
        Mono<Boolean> result = customerPersistenceAdapter.deleteCustomer("14sds");
        StepVerifier.create(result)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
        Mockito.verify(customerRepository,times(1)).delete(anyString());
    }

    @Test
    @DisplayName("When Delete Customer Failed Expect Do Not Deleted")
    void When_SavingCustomer_ExpectDoNotDeleted(){
        when(customerRepository.delete(anyString())).thenReturn(Mono.just(Boolean.FALSE));
        Mono<Boolean> result = customerPersistenceAdapter.deleteCustomer("14sds");
        StepVerifier.create(result)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        Mockito.verify(customerRepository,times(1)).delete(anyString());
    }

    @Test
    @DisplayName("When Customer Id Exists Expect Customer Information")
    void When_CustomerIdExists_Expect_CustomerInformation(){
        CustomerTemplate customerTemplate= TestUtilCustomer.buildCustomerTemplateMock();
        when(customerRepository.getCustomer(anyString())).thenReturn(Mono.just(customerTemplate));
        when(customerPersistenceMapper.customerTemplateTocustomer(any(CustomerTemplate.class))).thenReturn(TestUtilCustomer.buildCustomerMock());
        Mono<Customer> result = customerPersistenceAdapter.getCustomer("12345");
        StepVerifier.create(result)
                .expectNextMatches(customerMatch->{
                        System.out.println("customerTemplate "+customerMatch.getName());
                        return customerMatch.getId().equals(customerTemplate.getId())
                                && customerMatch.getName().equals(customerTemplate.getName())
                                && customerMatch.getLastName().equals(customerTemplate.getLastName())
                                && customerMatch.getAge().equals(customerTemplate.getAge())
                                && customerMatch.getBirthDate().equals(customerTemplate.getBirthDate());}
                ).verifyComplete();
        Mockito.verify(customerRepository,times(1)).getCustomer(anyString());
        Mockito.verify(customerPersistenceMapper,times(1)).customerTemplateTocustomer(any());
    }

}
