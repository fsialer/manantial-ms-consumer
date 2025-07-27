package com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.mappers;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.models.CustomerDocument;
import com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.models.CustomerTemplate;
import reactor.core.publisher.Mono;

public interface CustomerPersistenceMapper {
    Mono<Customer> customerDocumentMonoToCustomerMono(Mono<CustomerDocument> customerDocumentMono);
    CustomerDocument customerToCustomerDocument(Customer customer);
    CustomerTemplate customerToCustomerTemplate(Customer customer);
    Mono<Customer> customerTemplateMonoToCustomerMono(Mono<CustomerTemplate> customerTemplateMono);
}
