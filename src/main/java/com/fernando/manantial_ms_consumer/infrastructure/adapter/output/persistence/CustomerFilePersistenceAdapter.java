package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence;

import com.fernando.manantial_ms_consumer.application.ports.output.CustomerFilePersistencePort;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.CustomerFilePersistenceMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.CustomerFileRepository;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.repository.CustomerRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerFilePersistenceAdapter implements CustomerFilePersistencePort {
    private final CustomerFileRepository customerFileRepository;
    private final CustomerFilePersistenceMapper customerFilePersistenceMapper;

    @Override
    public Mono<Boolean> saveCustomerFile(CustomerFile customer) {
        return customerFileRepository.save(customerFilePersistenceMapper.customerFileToCustomerFileTemplate(customer));
    }

    @Override
    public Mono<CustomerFile> getCustomerFile(String key) {
        return customerFileRepository.getCustomerFile(key).map(customerFilePersistenceMapper::customerFileTemplateToCustomerFile);
    }

    @Override
    public Mono<Boolean> deleteCustomerFile(String key) {
        return customerFileRepository.delete(key);
    }
}
