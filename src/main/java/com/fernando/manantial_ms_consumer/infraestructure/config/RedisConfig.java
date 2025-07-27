package com.fernando.manantial_ms_consumer.infraestructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.models.CustomerTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisTemplate<String, CustomerTemplate> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializer<String> keySerializer = new StringRedisSerializer();

        // 👇 Crea y configura el ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Soporte para LocalDate
        objectMapper.findAndRegisterModules();

        // 👇 Pasa el mapper al constructor
        Jackson2JsonRedisSerializer<CustomerTemplate> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, CustomerTemplate.class);

        RedisSerializationContext<String, CustomerTemplate> context =
                RedisSerializationContext.<String, CustomerTemplate>newSerializationContext(keySerializer)
                        .value(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveValueOperations<String, CustomerTemplate> reactiveValueOperations(ReactiveRedisTemplate<String, CustomerTemplate> redisTemplate) {
        return redisTemplate.opsForValue();
    }
}
