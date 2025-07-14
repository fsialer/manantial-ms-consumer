package com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence;

import com.fernando.manantial_ms_consumer.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceAdapter implements CustomerPersistencePort {

    private final CustomerRepository customerRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    @Override
    public Mono<Customer> saveCustomer(Customer customer) {
        return customerPersistenceMapper
                .customerDocumentMonoToCustomerMono(customerRepository.save(customerPersistenceMapper.customerToCustomerDocument(customer)));
    }
}
