package com.main10.domain.reserve.pay;

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
public class PayApproveInfo {
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private Amount amount;
    private CardInfo cardInfo;
    private String itemName;
    private String itemCode;
    private String payload;
    private Integer quantity;
    private Integer taxFreeAmount;
    private Integer vatAmount;
    private Date createdAt;
    private Date approvedAt;
    private String orderStatus;

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
