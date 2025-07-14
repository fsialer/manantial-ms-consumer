package com.fernando.manantial_ms_consumer.domain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.manantial_ms_consumer.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements SaveCustomerUseCase {
    private final CustomerPersistencePort customerPersistencePort;
    private final ObjectMapper mapper;


    @Override
    @KafkaListener(topics = "customer-topic", groupId = "sse-group")
    public void save(String message) {
        try{
            Customer customer = mapper.readValue(message, Customer.class);
            Customer customerStream = Customer.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .lastName(customer.getLastName())
                    .age(customer.getAge())
                    .birthDate(customer.getBirthDate())
                    .build();
            customerPersistencePort.saveCustomer(customerStream).subscribe();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
