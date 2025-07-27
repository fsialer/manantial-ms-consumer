package com.fernando.manantial_ms_consumer.infraestructure.adapter.input.listener.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
        private String id;
        private String name;
        private String lastName;
        private Integer age;
        //@JsonFormat(pattern = "yyyy-MM-dd")
        private String birthDate;
        private String lifeExpectancy;
}
