package com.fernando.manantial_ms_consumer.utils;

import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;
import com.fernando.manantial_ms_consumer.infrastructure.adapter.output.persistence.models.CustomerFileTemplate;

public class TestUtilCustomerFile {
    public static CustomerFile buildCustomerFileMock(){
        return CustomerFile.builder()
                .id("sdsd545d1sd1sJohn")
                .path("/sd/example.pdf")
                .build();
    }

    public static CustomerFileTemplate buildCustomerFileTemplateMock(){
        return CustomerFileTemplate.builder()
                .id("sdsd545d1sd1sJohn")
                .path("/sd/example.pdf")
                .build();
    }

}
