package com.fernando.manantial_ms_consumer.domain.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFile {
    private String id;
    private String path;
    private String fileName;
    private String contentType;
}
