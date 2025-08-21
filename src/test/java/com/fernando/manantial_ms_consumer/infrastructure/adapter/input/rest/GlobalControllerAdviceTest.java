package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.rest;

import com.fernando.manantial_ms_consumer.application.ports.input.GetCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GetFileUseCase;
import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerFileNotFoundException;
import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.input.rest.model.response.ErrorResponse;
import com.fernando.manantial_ms_consumer.utils.TestUtilCustomerFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.fernando.manantial_ms_consumer.infrastructure.utils.ErrorCatalog.CUSTOMER_FILE_NOT_FOUND;
import static com.fernando.manantial_ms_consumer.infrastructure.utils.ErrorCatalog.CUSTOMER_INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(CustomerRestAdapter.class)
public class GlobalControllerAdviceTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetFileUseCase getFileUseCase;

    @MockitoBean
    private GetCustomerFileUseCase getCustomerFileUseCase;

    @Test
    @DisplayName("Expect CustomerNotFoundException When CustomerFile Not Found")
    void Expect_CustomerNotFoundException_When_CustomerFileNotFound() {
        String id = "1541ddsd";
        when(getCustomerFileUseCase.getCustomerFile(anyString())).thenThrow(new CustomerFileNotFoundException("Customer not found: "));
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/consumer/download")
                        .queryParam("id", id)
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response->{
                    assertEquals(response.code(),CUSTOMER_FILE_NOT_FOUND.getCode());
                    assertEquals(response.message(),CUSTOMER_FILE_NOT_FOUND.getMessage());
                });
        Mockito.verify(getCustomerFileUseCase,times(1)).getCustomerFile(anyString());
        Mockito.verify(getFileUseCase,times(0)).getFile(anyString());
    }

    @Test
    @DisplayName("Expect Exception When There Are An Error")
    void Expect_Exception_When_ThereAreAnError() {
        String id = "1541ddsd";
        byte[] content = "contenido".getBytes();
        CustomerFile customerFile= TestUtilCustomerFile.buildCustomerFileMock();
        when(getCustomerFileUseCase.getCustomerFile(anyString())).thenReturn(Mono.just(customerFile));
        when(getFileUseCase.getFile(anyString())).thenReturn(content);
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/consumer/download/2")
                        .queryParam("id", id)
                        .build())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response->{
                    assertEquals(response.code(),CUSTOMER_INTERNAL_SERVER_ERROR.getCode());
                    assertEquals(response.message(),CUSTOMER_INTERNAL_SERVER_ERROR.getMessage());
                });
        Mockito.verify(getCustomerFileUseCase,times(0)).getCustomerFile(anyString());
        Mockito.verify(getFileUseCase,times(0)).getFile(anyString());
    }
}
