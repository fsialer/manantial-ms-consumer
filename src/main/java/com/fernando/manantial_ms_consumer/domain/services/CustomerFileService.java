package com.fernando.manantial_ms_consumer.domain.services;

import com.fernando.manantial_ms_consumer.application.ports.input.DeleteCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GetCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GetFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerFilePersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerFileNotFoundException;
import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerFileService implements DeleteCustomerFileUseCase, GetFileUseCase, GetCustomerFileUseCase {

    private final CustomerFilePersistencePort customerFilePersistencePort;
    private final StoreFilePort storeFilePort;

    @Override
    public Mono<Void> delete(String id) {
        return customerFilePersistencePort.getCustomerFile(id)
                .switchIfEmpty(Mono.error(new CustomerFileNotFoundException("File not found.")))
                .flatMap(fileInfo-> customerFilePersistencePort.deleteCustomerFile(id)
                                .flatMap(fileDeleted -> {
                                    if (Boolean.TRUE.equals(fileDeleted)) {
                                        log.info("CustomerFile deleted correctly: {}", id);
                                        return Mono.fromRunnable(() -> storeFilePort.delete(fileInfo.getPath()));
                                    } else {
                                        log.warn("File for customer {} not deleted from Redis.", fileInfo.getPath());
                                        return Mono.empty();
                                    }
                                })
                )
                .doOnError(e -> log.error("Error deleting customer '{}': {}", id, e.getMessage()))
                .then();
    }

    @Override
    public byte[] getFile(String path) {
        return storeFilePort.getFile(path);
    }

    @Override
    public Mono<CustomerFile> getCustomerFile(String id) {
        return customerFilePersistencePort.getCustomerFile(id)
                .switchIfEmpty(Mono.error(new CustomerFileNotFoundException("File not found.")));
    }
}
