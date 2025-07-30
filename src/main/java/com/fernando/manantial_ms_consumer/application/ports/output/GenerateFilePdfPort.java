package com.fernando.manantial_ms_consumer.application.ports.output;

import com.fernando.manantial_ms_consumer.domain.models.Customer;

public interface GenerateFilePdfPort {
    byte[] generatePdfCustomer(Customer customer);
}
