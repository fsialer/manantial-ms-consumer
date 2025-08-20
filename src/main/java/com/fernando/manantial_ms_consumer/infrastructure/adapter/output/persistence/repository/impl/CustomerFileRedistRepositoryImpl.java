package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.impl;

import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerFileTemplate;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.CustomerFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerFileRedistRepositoryImpl implements CustomerFileRepository {
    private final ReactiveValueOperations<String, CustomerFileTemplate> reactiveRedisTemplate;
    @Override
    public Mono<CustomerFileTemplate> getCustomerFile(String key) {
        return reactiveRedisTemplate.get("file_customer:"+key)
                .doOnNext(c -> log.info("📦 Cliente obtenido desde Redis con key {}: {}", key, c))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("⚠️ Cliente no encontrado en Redis con key: {}", key);
                    return Mono.empty();
                }));
    }

    @Override
    public Mono<Boolean> save(CustomerFileTemplate customerFileTemplate) {
        return reactiveRedisTemplate.set("file_customer:"+ customerFileTemplate.getId(), customerFileTemplate);
    }

    @Override
    public Mono<Boolean> delete(String key) {
        return reactiveRedisTemplate.delete("file_customer:"+key);
    }
}
