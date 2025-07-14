package com.fernando.manantial_ms_consumer.application.ports.output;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import reactor.core.publisher.Mono;

public interface CustomerPersistencePort {
    Mono<Customer> saveCustomer(Customer customer);
}
