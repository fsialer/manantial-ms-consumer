package com.fernando.manantial_ms_consumer.domain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.manantial_ms_consumer.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements SaveCustomerUseCase {
    private final CustomerPersistencePort customerPersistencePort;

    @Override
    public Mono<Boolean> save(Customer customer) {
         return customerPersistencePort.saveCustomer(customer)
                 .doOnSuccess(result->log.info("Customer saved successfully: {}",customer.getId()))
                 .doOnError(e->log.error("Error saving customer: {}",e.getMessage()))
                 .onErrorReturn(Boolean.FALSE);
    }
}
