package com.fernando.manantial_ms_consumer.application.ports.input;

import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import reactor.core.publisher.Mono;

public interface GetCustomerFileUseCase {
    Mono<CustomerFile> getCustomerFile(String id);
}
