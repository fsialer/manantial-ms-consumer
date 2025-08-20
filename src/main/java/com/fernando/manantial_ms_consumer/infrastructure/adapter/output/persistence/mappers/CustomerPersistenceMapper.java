package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerTemplate;

public interface CustomerPersistenceMapper {
    CustomerTemplate customerToCustomerTemplate(Customer customer);
    Customer customerTemplateTocustomer(CustomerTemplate customer);
}
