package com.main10.domain.reserve.pay;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestForReserveInfo {
    private String cid;
    private String tid;
    private String pg_token;
    private String total_amount;
    private String partner_user_id;
    private String partner_order_id;

}