package com.main21.reserve.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;

import static com.main21.reserve.utils.PayConstants.*;

@Configuration
public class FeignConfig {

    @Value("${kakao.admin.key}")
    private String adminKey;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * feign 로깅 처리
     *
     * @return Logger.Level
     * @author mozzi327
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }


    @Bean
    @Primary
    Encoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(this.messageConverters));
    }


    /**
     * feign 커스텀 디코더
     *
     * @return Decoder
     * @author mozzi327
     */
    @Bean
    Decoder feignDecoder() {
        return new GithubDecoder();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate ->
                requestTemplate
                        .header(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, KAKAO_AK + adminKey)
                        .header(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + UTF_8);
    }
}
