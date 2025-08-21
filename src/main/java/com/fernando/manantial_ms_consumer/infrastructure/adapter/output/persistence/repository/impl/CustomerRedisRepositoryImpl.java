package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.impl;

import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerTemplate;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.CustomerRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerRedisRepositoryImpl implements CustomerRedisRepository {
    private final ReactiveValueOperations<String, CustomerTemplate> reactiveRedisTemplate;
    @Override
    public Mono<Boolean> save(CustomerTemplate customerTemplate) {
        return reactiveRedisTemplate.set("customers:"+customerTemplate.getId(), customerTemplate);
    }

    @Override
    public Mono<Boolean> delete(String key) {
        return reactiveRedisTemplate.delete("customers:"+key);
    }

    @Override
    public Mono<CustomerTemplate> getCustomer(String key) {
        log.info("key: {}",key);
        return reactiveRedisTemplate.get("customers"+key)
                .doOnNext(c -> log.info("📦 Cliente obtenido desde Redis con key {}: {} {}", key, c, c.getId()))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("⚠️ Cliente no encontrado en Redis con key: {}", key);
                    return Mono.empty();
                })).log();
    }


}
