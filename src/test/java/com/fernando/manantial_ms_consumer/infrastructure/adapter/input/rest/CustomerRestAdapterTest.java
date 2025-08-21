package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.rest;

import com.fernando.manantial_ms_consumer.application.ports.input.GetCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GetFileUseCase;
import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import com.fernando.manantial_ms_consumer.utils.TestUtilCustomerFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@WebFluxTest(CustomerRestAdapter.class)
class CustomerRestAdapterTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetFileUseCase getFileUseCase;

    @MockitoBean
    private GetCustomerFileUseCase getCustomerFileUseCase;

    @Test
    @DisplayName("When File Exists Expect Download File")
    void When_FileExists_Expect_DownloadFile() throws Exception {
        String id = "1541ddsd";
        byte[] content = "contenido".getBytes();
        CustomerFile customerFile= TestUtilCustomerFile.buildCustomerFileMock();
        when(getFileUseCase.getFile(anyString())).thenReturn(content);
        when(getCustomerFileUseCase.getCustomerFile(anyString())).thenReturn(Mono.just(customerFile));
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/consumer/download")
                        .queryParam("id", id)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Disposition", "attachment; filename=\"file\"")
                .expectHeader().contentType("application/pdf")
                .expectBody()
                .equals(content);
    }



}
