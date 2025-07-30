package com.fernando.manantial_ms_consumer.domain.services;

import com.fernando.manantial_ms_consumer.application.ports.input.GeneratePdfCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.GenerateFilePdfPort;
import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements SaveCustomerUseCase, GeneratePdfCustomerUseCase {
    private final CustomerPersistencePort customerPersistencePort;
    private final GenerateFilePdfPort generateFilePdfPort;
    private final StoreFilePort storeFilePort;

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
        storeFilePort.store("customer_"+customer.getId()+".pdf",pdf);
        log.info("PDF generated and stored for customer: {} ({})",customer.getId(),"customer_"+customer.getId()+".pdf");
    }
}
