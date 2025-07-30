package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.listener.model.request;

import lombok.*;

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
