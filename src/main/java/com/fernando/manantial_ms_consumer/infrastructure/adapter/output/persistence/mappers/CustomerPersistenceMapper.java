package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerDocument;
import reactor.core.publisher.Mono;

public interface CustomerPersistenceMapper {
    CustomerDocument customerToCustomerDocument(Customer customer);
    Mono<Customer> customerDocumentMonoToCustomerMono(Mono<CustomerDocument> customer);
}
