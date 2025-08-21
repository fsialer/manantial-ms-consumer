package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.rest.model.response;

import com.fernando.manantial_ms_consumer.domain.enums.ErrorType;
import lombok.Builder;

import java.util.List;

@Builder
public record ErrorResponse(String code, ErrorType type, String message, List<String> details, String timestamp)  {
}
