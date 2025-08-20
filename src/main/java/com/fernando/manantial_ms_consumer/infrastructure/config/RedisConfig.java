package com.fernando.manantial_ms_consumer.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerFileTemplate;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerTemplate;
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
    public ReactiveRedisTemplate<String, CustomerTemplate> customerTemplateRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        Jackson2JsonRedisSerializer<CustomerTemplate> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, CustomerTemplate.class);

        RedisSerializationContext<String, CustomerTemplate> context =
                RedisSerializationContext.<String, CustomerTemplate>newSerializationContext(keySerializer)
                        .value(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveValueOperations<String, CustomerTemplate> customerTemplateValueOperations(
            ReactiveRedisTemplate<String, CustomerTemplate> customerTemplateRedisTemplate) {
        return customerTemplateRedisTemplate.opsForValue();
    }

    @Bean
    public ReactiveRedisTemplate<String, CustomerFileTemplate> customerFileTemplateRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        Jackson2JsonRedisSerializer<CustomerFileTemplate> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, CustomerFileTemplate.class);

        RedisSerializationContext<String, CustomerFileTemplate> context =
                RedisSerializationContext.<String, CustomerFileTemplate>newSerializationContext(keySerializer)
                        .value(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveValueOperations<String, CustomerFileTemplate> customerFileTemplateValueOperations(
            ReactiveRedisTemplate<String, CustomerFileTemplate> customerFileTemplateRedisTemplate) {
        return customerFileTemplateRedisTemplate.opsForValue();
    }
}
