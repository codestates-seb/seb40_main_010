package com.main10.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.TimeZone;

public class RedisInitializeConfig {

    @Autowired
    private CacheManager cacheManager;

    @PostConstruct
    public void onApplicationEvent(ApplicationReadyEvent event) {
        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(n -> Objects.requireNonNull(cacheManager.getCache(n)).clear());
    }
//
//    @PostConstruct
//    public void started() {
//        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
//    }

}
