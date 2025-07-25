package com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.repository;

import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.models.CustomerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<CustomerDocument, String> {

}
