package com.fernando.manantial_ms_consumer.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    CUSTOMER_INTERNAL_SERVER_ERROR("CONSUMER_000", "Internal server error."),
    CUSTOMER_FILE_NOT_FOUND("CONSUMER_002","Customer file not found.");
    private final String code;
    private final String message;
}
