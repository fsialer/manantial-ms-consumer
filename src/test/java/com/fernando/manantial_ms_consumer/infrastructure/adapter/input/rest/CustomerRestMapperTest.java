package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.rest;

import com.fernando.manantial_ms_consumer.application.ports.input.GetFileUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;


@WebFluxTest(CustomerRestAdapter.class)
class CustomerRestAdapterTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetFileUseCase getFileUseCase;

    @Test
    @DisplayName("When File Exists Expect Download File")
    void When_FileExists_Expect_DownloadFile() throws Exception {
        String filePath = "test/file.pdf";
        byte[] content = "contenido".getBytes();

        when(getFileUseCase.getFile(filePath)).thenReturn(content);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/consumer/download")
                        .queryParam("filePath", filePath)
                        .queryParam("fileName", "archivo.pdf")
                        .queryParam("contentType", "application/pdf")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Disposition", "attachment; filename=\"archivo.pdf\"")
                .expectHeader().contentType("application/pdf")
                .expectBody()
                .equals(content);
    }

    @Test
    @DisplayName("When File Exists And Request Name Expect Download File")
    void When_FileExistsAndRequestName_Expect_DownloadFile() throws Exception {
        String filePath = "test/file.pdf";
        byte[] content = "contenido".getBytes();

        when(getFileUseCase.getFile(filePath)).thenReturn(content);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/consumer/download")
                        .queryParam("filePath", filePath)
                        .queryParam("contentType", "application/pdf")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Disposition", "attachment; filename=\"file.pdf\"")
                .expectHeader().contentType("application/pdf")
                .expectBody()
                .equals(content);
    }

}
