package com.fernando.manantial_ms_consumer.domain.services;

import com.fernando.manantial_ms_consumer.application.ports.input.DeleteCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GeneratePdfCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerFilePersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.GenerateFilePdfPort;
import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerNotFoundException;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements SaveCustomerUseCase, GeneratePdfCustomerUseCase, DeleteCustomerUseCase {

    @Value("${file_path.customer.information}")
    private String customerInformationPath;
    private final CustomerPersistencePort customerPersistencePort;
    private final GenerateFilePdfPort generateFilePdfPort;
    private final StoreFilePort storeFilePort;
    private final CustomerFilePersistencePort customerFilePersistencePort;

    @Override
    public Mono<Boolean> save(Customer customer) {
         return customerPersistencePort.saveCustomer(customer)
                 .doOnSuccess(result->log.info("Customer {} successfully: {}",Boolean.TRUE.equals(result)?"saved":"do not saved",customer.getId()))
                 .doOnError(e->log.error("Error saving customer: {}",e.getMessage()))
                 .onErrorReturn(Boolean.FALSE);
    }

    @Override
    public void generatePdfCustomer(Customer customer) {
        byte[] pdf= generateFilePdfPort.generatePdfCustomer(customer);
        String fileName="customer_"+customer.getId()+".pdf";
        String pathFull=customerInformationPath.concat("/").concat(fileName);
        String contentType= "application/pdf";
        customerFilePersistencePort.saveCustomerFile(new CustomerFile(customer.getId(),pathFull,fileName,contentType))
                .flatMap(saved->{
                    if(Boolean.TRUE.equals(saved)){
                        return Mono.fromRunnable(() -> {
                            storeFilePort.store(fileName,pdf, customerInformationPath,contentType);
                            log.info("PDF generated and stored for customer: {} ({})",customer.getId(),fileName);
                        } );
                    }else{
                        log.error("File not deleted correctly: {}",  customer.getId());
                        return Mono.empty();
                    }
                }).subscribe();
    }

    @Override
    public Mono<Void> delete(String id) {
        return customerPersistencePort.getCustomer(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found.")))
                .flatMap(customer ->
                        customerPersistencePort.deleteCustomer(id)
                                .flatMap(deleted -> {
                                    log.info("Customer deleted correctly: {}", id);
                                    if (Boolean.FALSE.equals(deleted)) {
                                        log.error("Customer not deleted correctly: {}",  id);
                                    }
                                    return Mono.empty();
                                })
                )
                .doOnError(e -> log.error("Error deleting customer '{}': {}", id, e.getMessage()))
                .then();
    }
}
