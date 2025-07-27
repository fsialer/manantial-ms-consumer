package com.fernando.manantial_ms_consumer.application.ports.input;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import reactor.core.publisher.Mono;

public interface SaveCustomerUseCase {
    Mono<Boolean> save(Customer customer);
}
