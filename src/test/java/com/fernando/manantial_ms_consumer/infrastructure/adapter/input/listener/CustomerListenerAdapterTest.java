package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener;

import com.fernando.manantial_ms_consumer.application.ports.input.DeleteCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GeneratePdfCustomerUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener.mapper.CustomerListenerMapper;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener.model.request.CustomerRequest;
import com.fernando.manantial_ms_consumer.utils.TestUtilCustomer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerListenerAdapterTest {
    @InjectMocks
    private CustomerListenerAdapter listener;

    @Mock
    private GeneratePdfCustomerUseCase generatePdfCustomerUseCase;

    @Mock
    private CustomerListenerMapper customerListenerMapper;

    @Mock
    private DeleteCustomerFileUseCase deleteCustomerFileUseCase;

    @Test
    @DisplayName("When Receive Message To Customer Topic Expect Save Customer Correctly")
    void When_ReceiveMessageToCustomerTopic_Expect_SaveCustomerCorrectly() {
        String json = "{\"name\":\"John\",\"id\":1,\"lastName\":\"Doe\",\"age\":33,\"birthDay\":\"1991-10-01\"}";
        CustomerRequest customerRequest = TestUtilCustomer.buildCustomerRequestMock();
        Customer customer = TestUtilCustomer.buildCustomerMock();

        when(customerListenerMapper.stringToCustomer(json)).thenReturn(customerRequest);
        when(customerListenerMapper.customerRequestToCustomer(customerRequest)).thenReturn(customer);
        when(generatePdfCustomerUseCase.generatePdfCustomer(any())).thenReturn(Mono.empty());

        ConsumerRecord<String, String> record = new ConsumerRecord<>("customer-topic", 0, 0L, null, json);
        listener.receiveMessageCustomerPdf(record);
        verify(generatePdfCustomerUseCase).generatePdfCustomer(customer);
    }

    @Test
    @DisplayName("When Receive Message To Delete Customer Topic Expect DeleteCustomer")
     void When_ReceiveMessageToDeleteCustomerTopic_Expect_DeleteCustomer() {
        String key = "141";
        when(deleteCustomerFileUseCase.delete(anyString())).thenReturn(Mono.empty());
        ConsumerRecord<String, String> record = new ConsumerRecord<>("delete-customer-topic", 0, 0L, null, key);
        listener.receiveMessageDeleteCustomerFile(record);
        verify(deleteCustomerFileUseCase,times(1)).delete(anyString());
    }
}
