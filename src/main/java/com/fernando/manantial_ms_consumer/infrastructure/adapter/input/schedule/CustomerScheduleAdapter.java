package com.fernando.manantial_ms_consumer.infrastructure.adapter.input.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CustomerScheduleAdapter {

    @Scheduled(cron = "0 0 8 * * *")
    public void scheduleCongratulatoryEmail(){

    }
}
