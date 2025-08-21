package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.rest;

import com.fernando.manantial_ms_consumer.domain.exceptions.CustomerFileNotFoundException;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.input.rest.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.fernando.manantial_ms_consumer.domain.enums.ErrorType.FUNCTIONAL;
import static com.fernando.manantial_ms_consumer.domain.enums.ErrorType.SYSTEM;
import static com.fernando.manantial_ms_consumer.infrastructure.utils.ErrorCatalog.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerFileNotFoundException.class)
    public Mono<ErrorResponse> handleCustomerFileNotFoundExceptionException(
            CustomerFileNotFoundException e) {
        log.warn("⚠️ Warning by rule ({}): {}",CUSTOMER_FILE_NOT_FOUND.getCode(),e.getMessage());
        return Mono.just(ErrorResponse.builder()
                .code(CUSTOMER_FILE_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(CUSTOMER_FILE_NOT_FOUND.getMessage())
                .details(List.of(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleException(Exception e) {
        log.error("❌ Error ({}): {}",CUSTOMER_INTERNAL_SERVER_ERROR.getCode(),e.getMessage());
        return Mono.just(ErrorResponse.builder()
                .code(CUSTOMER_INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(CUSTOMER_INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }
}
