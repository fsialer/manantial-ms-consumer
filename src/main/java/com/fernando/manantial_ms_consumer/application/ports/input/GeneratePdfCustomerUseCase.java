package com.fernando.manantial_ms_consumer.application.ports.input;

import com.fernando.manantial_ms_consumer.domain.models.Customer;

public interface GeneratePdfCustomerUseCase {
    void generatePdfCustomer(Customer customer);
}
