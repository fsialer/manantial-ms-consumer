package com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.mappers.impl;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.models.CustomerDocument;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomerPersistenceMapperImpl implements CustomerPersistenceMapper {

    @Override
    public Mono<Customer> customerDocumentMonoToCustomerMono(Mono<CustomerDocument> customerDocumentMono) {
        return customerDocumentMono.flatMap(customer->
                Mono.just(Customer.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .age(customer.getAge())
                        .birthDate(customer.getBirthDate())
                        .build())
        );
    }

    @Override
    public CustomerDocument customerToCustomerDocument(Customer customer) {
        return CustomerDocument.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastName(customer.getLastName())
                .age(customer.getAge())
                .birthDate(customer.getBirthDate())
                .build();
    }
}
