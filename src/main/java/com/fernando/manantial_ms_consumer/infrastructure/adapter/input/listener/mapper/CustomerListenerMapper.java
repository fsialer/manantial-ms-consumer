package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener.mapper;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener.model.request.CustomerRequest;


public interface CustomerListenerMapper {
    CustomerRequest stringToCustomer(String message);
    Customer customerRequestToCustomer(CustomerRequest customerRequest);
}
