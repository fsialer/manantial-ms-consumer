package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository;

import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerTemplate;
import reactor.core.publisher.Mono;

public interface CustomerRedisRepository  {
    Mono<Boolean> save(CustomerTemplate customerTemplate);
    Mono<Boolean> delete(String key);
    Mono<CustomerTemplate> getCustomer(String key);
}
