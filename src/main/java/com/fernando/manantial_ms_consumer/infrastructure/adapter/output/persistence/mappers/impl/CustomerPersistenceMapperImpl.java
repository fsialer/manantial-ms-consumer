package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.impl;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerDocument;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomerPersistenceMapperImpl implements CustomerPersistenceMapper {

    @Override
    public CustomerDocument customerToCustomerDocument(Customer customer) {
        return CustomerDocument.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastName(customer.getLastName())
                .age(customer.getAge())
                .birthDate(customer.getBirthDate())
                .pathFile(customer.getPathFile())
                .build();
    }

    @Override
    public Mono<Customer> customerDocumentMonoToCustomerMono(Mono<CustomerDocument> customerMono) {
        return customerMono.flatMap(customer->{
            return Mono.just(Customer.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .lastName(customer.getLastName())
                    .age(customer.getAge())
                    .birthDate(customer.getBirthDate())
                    .pathFile(customer.getPathFile())
                    .build());
        });
    }


}
