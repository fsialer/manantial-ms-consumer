package com.fernando.manantial_ms_consumer.utils;

import com.fernando.manantial_ms_consumer.domain.models.CustomerFile;

public class TestUtilCustomerFile {
    public static CustomerFile buildCustomerFileMock(){
        return CustomerFile.builder()
                .id("sdsd545d1sd1sJohn")
                .path("/sd/example.pdf")
                .fileName("file")
                .contentType("application/pdf")
                .build();
    }

}
