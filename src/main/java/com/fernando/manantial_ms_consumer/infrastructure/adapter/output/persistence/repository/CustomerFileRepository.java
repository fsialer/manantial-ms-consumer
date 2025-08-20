package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository;

import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerFileTemplate;
import reactor.core.publisher.Mono;

public interface CustomerFileRepository {
    Mono<CustomerFileTemplate> getCustomerFile(String key);
    Mono<Boolean> save(CustomerFileTemplate customerFileTemplate);
    Mono<Boolean> delete(String key);
}
