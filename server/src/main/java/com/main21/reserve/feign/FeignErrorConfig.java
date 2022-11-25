package com.main21.reserve.feign;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
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
