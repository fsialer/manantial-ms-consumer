package com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.repository;

import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.models.CustomerDocument;
import reactor.core.publisher.Mono;

public interface CustomerRepository  {
    Mono<CustomerDocument> save(CustomerDocument customerDocument);
}
