package com.fernando.manantial_ms_consumer.utils;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener.model.request.CustomerRequest;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerTemplate;

public class TestUtilCustomer {
    public static Customer buildCustomerMock(){
        return Customer.builder()
                .id("12345")
                .name("John")
                .lastName("Doe")
                .age(33)
                .birthDate("1991-10-01")
                .build();
    }

    public static CustomerTemplate buildCustomerTemplateMock(){
        return CustomerTemplate.builder()
                .id("12345")
                .name("John")
                .lastName("Doe")
                .age(33)
                .birthDate("1991-10-01")
                .build();
    }

    public static CustomerRequest buildCustomerRequestMock(){
        return CustomerRequest.builder()
                .id("12345")
                .name("John")
                .lastName("Doe")
                .age(33)
                .birthDate("1991-10-01")
                .build();
    }
}
