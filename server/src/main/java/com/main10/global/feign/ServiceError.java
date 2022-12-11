package com.main10.global.feign;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServiceError {
    private String title;
    private String description;

    @Builder
    public ServiceError(String title,
                        String description) {
        this.title = title;
        this.description = description;
    }
}
