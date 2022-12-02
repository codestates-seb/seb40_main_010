package com.main10.domain.reserve.pay;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoHeaders {
    private String accept;
    private String adminKey;
    private String contentType;
}
