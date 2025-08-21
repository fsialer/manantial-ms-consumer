package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFileTemplate {
    private String id;
    private String path;
    private String fileName;
    private String contentType;
}
