package com.main10.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@TestConfiguration
public class TimeConfig {

    @Bean
    public TimeZone timeZone(){
        return TimeZone.getTimeZone("UTC");
    }
}
