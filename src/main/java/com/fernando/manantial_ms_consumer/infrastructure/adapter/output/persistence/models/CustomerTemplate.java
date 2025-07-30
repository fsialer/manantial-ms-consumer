package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTemplate {
    private String id;
    private String name;
    private String lastName;
    private Integer age;
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private String birthDate;
}
