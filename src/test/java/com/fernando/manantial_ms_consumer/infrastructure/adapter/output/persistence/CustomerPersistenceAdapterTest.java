package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerDocument;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;

    @Mock
    private CustomerPersistenceMapper customerPersistenceMapper;

    @InjectMocks
    private CustomerPersistenceAdapter customerPersistenceAdapter;

    @Test
    @DisplayName("When Saving Customer Expect Saved Successfully")
    void When_SavingCustomer_ExpectSavedSuccessfully(){
        Customer customer= TestUtilCustomer.buildCustomerMock();
        CustomerDocument customerDocument= TestUtilCustomer.buildCustomerDocumentMock();
        when(customerRepository.save(any(CustomerDocument.class))).thenReturn(Mono.just(customerDocument));
        when(customerPersistenceMapper.customerDocumentMonoToCustomerMono(any())).thenReturn(Mono.just(customer));
        when(customerPersistenceMapper.customerToCustomerDocument(any(Customer.class))).thenReturn(customerDocument);
        Mono<Customer> result = customerPersistenceAdapter.saveCustomer(customer);
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
        Mockito.verify(customerRepository,times(1)).save(any(CustomerDocument.class));
        Mockito.verify(customerPersistenceMapper,times(1)).customerToCustomerDocument(any(Customer.class));
    }


    @Test
    @DisplayName("When Customer Id Exists Expect Customer Information")
    void When_CustomerIdExists_Expect_CustomerInformation(){
        CustomerDocument customerDocument= TestUtilCustomer.buildCustomerDocumentMock();
        when(customerRepository.findById(anyString())).thenReturn(Mono.just(customerDocument));
        when(customerPersistenceMapper.customerDocumentMonoToCustomerMono(any())).thenReturn(Mono.just(TestUtilCustomer.buildCustomerMock()));
        Mono<Customer> result = customerPersistenceAdapter.getCustomer("12345");
        StepVerifier.create(result)
                .expectNextMatches(customerMatch->{
                        System.out.println("customerTemplate "+customerMatch.getName());
                        return customerMatch.getId().equals(customerDocument.getId())
                                && customerMatch.getName().equals(customerDocument.getName())
                                && customerMatch.getLastName().equals(customerDocument.getLastName())
                                && customerMatch.getAge().equals(customerDocument.getAge())
                                && customerMatch.getBirthDate().equals(customerDocument.getBirthDate());}
                ).verifyComplete();
        Mockito.verify(customerRepository,times(1)).findById(anyString());
        Mockito.verify(customerPersistenceMapper,times(1)).customerDocumentMonoToCustomerMono(any());
    }

}
