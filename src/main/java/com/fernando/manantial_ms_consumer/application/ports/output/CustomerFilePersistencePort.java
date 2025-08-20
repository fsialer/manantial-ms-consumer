package com.fernando.manantial_ms_consumer.application.ports.output;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import reactor.core.publisher.Mono;

public interface CustomerFilePersistencePort {
    Mono<Boolean> saveCustomerFile(CustomerFile customer);
    Mono<CustomerFile> getCustomerFile(String key);
    Mono<Boolean> deleteCustomerFile(String key);
}
