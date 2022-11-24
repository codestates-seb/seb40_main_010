package com.main21.reserve.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        if (template.body() == null) {
            log.info("NONE TEMPLETE BODY");
            return;
        }

    }
}
