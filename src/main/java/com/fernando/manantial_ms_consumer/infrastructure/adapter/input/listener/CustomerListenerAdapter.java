package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener;

import com.fernando.manantial_ms_consumer.application.ports.input.DeleteCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GeneratePdfCustomerUseCase;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener.mapper.CustomerListenerMapper;
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
    private final CustomerListenerMapper customerListenerMapper;
    private final GeneratePdfCustomerUseCase generatePdfCustomerUseCase;
    private final DeleteCustomerFileUseCase deleteCustomerFileUseCase;

    @KafkaListener(topics = "customer-topic", groupId = "customer-service-a", containerFactory = "strContainerFactory")
    public void receiveMessageCustomerPdf(ConsumerRecord<String, String> record){
        Mono.just(record.value())
                .flatMap(json -> {
                    return generatePdfCustomerUseCase.generatePdfCustomer(customerListenerMapper.customerRequestToCustomer(customerListenerMapper.stringToCustomer(json)));
                })
                .subscribe();
    }

    @KafkaListener(topics = "delete-customer-topic", groupId = "delete-customer-service-a", containerFactory = "strContainerFactory")
    public void receiveMessageDeleteCustomerFile(ConsumerRecord<String, String> record){
        Mono.just(record.value().replace("\"",""))
                .flatMap(deleteCustomerFileUseCase::delete)
                .subscribe();
    }
}
