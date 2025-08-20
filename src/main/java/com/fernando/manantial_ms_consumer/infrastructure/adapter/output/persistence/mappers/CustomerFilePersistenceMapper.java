package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers;

import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerFileTemplate;

public interface CustomerFilePersistenceMapper {

    CustomerFileTemplate customerFileToCustomerFileTemplate(CustomerFile customerFile);
    CustomerFile customerFileTemplateToCustomerFile(CustomerFileTemplate customerFile);
}
