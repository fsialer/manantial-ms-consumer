package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener.model.request;

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
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDate;
        private String lifeExpectancy;
        private String pathFile;
}
