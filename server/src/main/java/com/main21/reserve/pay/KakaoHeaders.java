package com.main21.reserve.pay;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoHeaders {
    private String accept;
    private String adminKey;
    private String contentType;
}
