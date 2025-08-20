package com.fernando.manantial_ms_consumer.application.ports.input;

import reactor.core.publisher.Mono;

public interface DeleteCustomerFileUseCase {
    Mono<Void> delete(String key);
}
