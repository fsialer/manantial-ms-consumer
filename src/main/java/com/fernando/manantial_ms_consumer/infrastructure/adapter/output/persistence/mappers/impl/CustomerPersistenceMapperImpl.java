package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.impl;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceMapperImpl implements CustomerPersistenceMapper {

    @Override
    public CustomerTemplate customerToCustomerTemplate(Customer customer) {
        return CustomerTemplate.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastName(customer.getLastName())
                .age(customer.getAge())
                .birthDate(customer.getBirthDate())
                .build();
    }

    @Override
    public Customer customerTemplateTocustomer(CustomerTemplate customerTemplate) {
        return Customer.builder()
                .id(customerTemplate.getId())
                .name(customerTemplate.getName())
                .lastName(customerTemplate.getLastName())
                .age(customerTemplate.getAge())
                .birthDate(customerTemplate.getBirthDate())
                .build();
    }


}
