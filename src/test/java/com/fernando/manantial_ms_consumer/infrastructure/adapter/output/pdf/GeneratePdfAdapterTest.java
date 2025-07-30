package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.pdf;

import com.fernando.manantial_ms_consumer.domain.models.Customer;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.file.GenerateField;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.file.GenerateFilePdfAdapter;
import com.fernando.manantial_ms_consumer.utils.TestUtilCustomer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratePdfAdapterTest {
    @InjectMocks
    private GenerateFilePdfAdapter pdfAdapter;

    @Mock
    private GenerateField<Customer> generateField;

    @Test
    @DisplayName("When Receive Customer Expect Generate A File In Bytes")
    void When_ReceiveCustomer_Expect_GenerateAFileInBytes() {
        Customer customer= TestUtilCustomer.buildCustomerMock();
        when(generateField.generate(any())).thenReturn(new byte[1]);
        byte[] pdfBytes = pdfAdapter.generatePdfCustomer(customer);
        assertEquals(1,pdfBytes.length);
        Mockito.verify(generateField,times(1)).generate(any());
    }
}
