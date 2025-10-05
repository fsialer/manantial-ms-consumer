package com.fernando.manantial_ms_consumer.domain.services;

import com.fernando.manantial_ms_consumer.application.ports.input.DeleteCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GeneratePdfCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_consumer.application.ports.output.GenerateFilePdfPort;
import com.fernando.manantial_ms_consumer.application.ports.output.StoreFilePort;
import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerNotFoundException;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements GeneratePdfCustomerUseCase, DeleteCustomerFileUseCase
{

    @Value("${file_path.customer.information}")
    private String customerInformationPath;
    private final CustomerPersistencePort customerPersistencePort;
    private final GenerateFilePdfPort generateFilePdfPort;
    private final StoreFilePort storeFilePort;

    @Override
    public Mono<Void> generatePdfCustomer(Customer customer) {
        byte[] pdf= generateFilePdfPort.generatePdfCustomer(customer);
        String fileName="customer_"+customer.getId()+".pdf";
        String pathFull=customerInformationPath.concat("/").concat(fileName);
        String contentType= "application/pdf";

        return customerPersistencePort.getCustomer(customer.getId())
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found.")))
                        .flatMap(customerInfo->{
                            storeFilePort.store(fileName,pdf, customerInformationPath,contentType);
                            log.info("PDF generated and stored for customer: {} ({})",customer.getId(),fileName);
                            customerInfo.setPathFile(pathFull);
                            return customerPersistencePort.saveCustomer(customerInfo);
                        }).then();
    }

    @Override
    public Mono<Void> delete(String path) {
        return Mono.fromRunnable(() -> storeFilePort.delete(path));

    }
}
