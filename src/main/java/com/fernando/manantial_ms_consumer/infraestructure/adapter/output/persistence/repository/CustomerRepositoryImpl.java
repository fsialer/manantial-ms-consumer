package com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.models.CustomerDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomerRepositoryImpl implements CustomerRepository{
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<CustomerDocument> save(CustomerDocument customerDocument) {
        try {
            String json = objectMapper.writeValueAsString(customerDocument);
            return redisTemplate.opsForValue()
                    .set(customerDocument.getId(), json)
                    .flatMap(saved -> Mono.just(customerDocument));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
