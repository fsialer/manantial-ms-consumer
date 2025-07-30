package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.file;

import com.fernando.manantial_ms_consumer.application.ports.output.GenerateFilePdfPort;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenerateFilePdfAdapter implements GenerateFilePdfPort {
    private final GenerateField<Customer> generateField;
    @Override
    public byte[] generatePdfCustomer(Customer customer) {
        return generateField.generate(customer);
    }
}
