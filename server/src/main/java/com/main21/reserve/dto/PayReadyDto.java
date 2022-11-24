package com.main21.reserve.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayReadyDto {

    private String tid;
    private String nextRedirectApiUrl;
    private String nextRedirectMobileUrl;
    private String nextRedirectPcUrl;
    private String androidAppScheme;
    private String iosAppScheme;
    private Date createdAt;
//
//
//    private String partner_order_id;
//    private String partner_user_id;
//    private String item_name;
//    private Integer total_amount;
//    private Integer tax_free_amount;
//    private String tid;
//    private String next_redirect_pc_url;
//    private Date created_at;
}
