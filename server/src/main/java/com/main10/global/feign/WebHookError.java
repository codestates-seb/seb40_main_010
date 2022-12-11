package com.main10.global.feign;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WebHookError {
    private String content;
    private boolean tts;
    private List<ServiceError> embeds;

    @Builder
    public WebHookError(String content,
                        boolean tts,
                        List<ServiceError> embeds) {
        this.content = content;
        this.tts = tts;
        this.embeds = embeds;
    }
}
