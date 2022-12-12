package com.main10.global.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "webhook-logging", url = "${feign.logging.url}")
public interface WebHookFeign {
    @PostMapping(value = "${feign.logging.server}")
    void sendServerLogging(@RequestHeader("Accept") String contentType,
                     WebHookError webHookError);

    @PostMapping(value = "${feign.logging.service}")
    void sendServiceLogging(@RequestHeader("Accept") String contentType,
                     WebHookError webHookError);
}
