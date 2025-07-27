package com.fernando.manantial_ms_consumer.infraestructure.adapter.input.listener.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.input.listener.mapper.CustomerListenerMapper;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.input.listener.model.request.CustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerListenerMapperImpl implements CustomerListenerMapper {
    private final ObjectMapper mapper;

    @Override
    public CustomerRequest stringToCustomer(String message) {
        try{
            CustomerRequest customer = mapper.readValue(message, CustomerRequest.class);
            return CustomerRequest.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .lastName(customer.getLastName())
                    .age(customer.getAge())
                    .birthDate(customer.getBirthDate())
                    .lifeExpectancy(customer.getLifeExpectancy())
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer customerRequestToCustomer(CustomerRequest customerRequest) {
        return Customer.builder()
                .id(customerRequest.getId())
                .name(customerRequest.getName())
                .lastName(customerRequest.getLastName())
                .age(customerRequest.getAge())
                .birthDate(customerRequest.getBirthDate())
                .build();
    }
}
