package com.fernando.manantial_ms_consumer.infraestructure.adapter.output.persistence.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

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
