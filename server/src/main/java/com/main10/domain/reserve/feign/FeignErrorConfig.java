package com.main10.domain.reserve.feign;

import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class FeignErrorConfig {
    @Bean
    public ErrorDecoder decoder() {
        return (methodKey, response) -> {
            if (log.isErrorEnabled()) {
                log.error("{} 요청이 성공하지 못했습니다. requestUrl: {}, requestBody: {}, responseBody: {}",
                        methodKey,
                        response.request().url(),
                        FeignClientResponseUtils.getRequestBody(response),
                        FeignClientResponseUtils.getResponseBody(response));
            }
            return new BusinessLogicException(ExceptionCode.PAYMENT_URL_REQUEST_FAILED);
        };
    }
}
