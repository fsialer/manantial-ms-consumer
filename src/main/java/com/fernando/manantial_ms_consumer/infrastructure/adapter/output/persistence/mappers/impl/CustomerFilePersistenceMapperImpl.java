package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.impl;

import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.CustomerFilePersistenceMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerFileTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerFilePersistenceMapperImpl implements CustomerFilePersistenceMapper {
    @Override
    public CustomerFileTemplate customerFileToCustomerFileTemplate(CustomerFile customerFile) {
        return CustomerFileTemplate.builder()
                .id(customerFile.getId())
                .path(customerFile.getPath())
                .build();
    }

    @Override
    public CustomerFile customerFileTemplateToCustomerFile(CustomerFileTemplate customerFile) {
        return CustomerFile.builder()
                .id(customerFile.getId())
                .path(customerFile.getPath())
                .build();
    }
}
