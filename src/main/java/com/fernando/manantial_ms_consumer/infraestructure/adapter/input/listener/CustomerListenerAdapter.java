package com.fernando.manantial_ms_consumer.infraestructure.adapter.input.listener;

import com.fernando.manantial_ms_consumer.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.input.listener.mapper.CustomerListenerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerListenerAdapter {
    private final SaveCustomerUseCase saveCustomerUseCase;
    private final CustomerListenerMapper customerListenerMapper;

    @KafkaListener(topics = "customer-topic", groupId = "customer-service")
    public void receiveMessageCustomer(ConsumerRecord<String, String> record){
        Mono.just(record.value())
                .flatMap(json -> saveCustomerUseCase.save(customerListenerMapper.customerRequestToCustomer(customerListenerMapper.stringToCustomer(json))))
                    .subscribe();
    }
}
