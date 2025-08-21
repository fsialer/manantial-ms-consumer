package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.rest;

import com.fernando.manantial_ms_consumer.application.ports.input.GetCustomerFileUseCase;
import com.fernando.manantial_ms_consumer.application.ports.input.GetFileUseCase;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/consumer")
@RequiredArgsConstructor
public class CustomerRestAdapter {
    private final GetFileUseCase getFileUseCase;
    private final GetCustomerFileUseCase getCustomerFileUseCase;

    @GetMapping("/download")
    public Mono<ResponseEntity<Resource>> downloadFile(
            @RequestParam String id
    ){
        return
            getCustomerFileUseCase.getCustomerFile(id)
                    .map(customerFile->{
                        byte[] data = getFileUseCase.getFile(customerFile.getPath()); // sigue siendo bloqueante
                        ByteArrayResource resource = new ByteArrayResource(data);
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + customerFile.getFileName() + "\"")
                                .contentType(MediaType.parseMediaType(customerFile.getContentType()))
                                .body(resource);
                    });
    }
}
