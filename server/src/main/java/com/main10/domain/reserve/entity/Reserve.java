package com.main10.domain.reserve.entity;

import com.main10.domain.reserve.pay.ReadyToPaymentInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

    private int capacity;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long placeId;

    private Long memberId;

    private Long totalCharge;

    @Enumerated(value = EnumType.STRING)
    private ReserveStatus status = ReserveStatus.PAY_IN_PROGRESS;

    public void addPlaceId(Long placeId) {
        this.placeId = placeId;
    }


    /************************** 주문 내역 정보 **************************/

    private String cid;

    private String tid;

    private String partnerOrderId;

    private String partnerUserId;

    private String itemName;

    private String quantity;

    private String totalAmount;

    private String valAmount;

    private String taxFreeAmount;

    private String approvalUrl;

    private String failUrl;

    private String cancelUrl;

    public void setPaymentInfo(ReadyToPaymentInfo params, String tid) {
        this.cid = params.getCid();
        this.tid = tid;
        this.partnerOrderId = params.getPartner_order_id();
        this.partnerUserId = params.getPartner_user_id();
        this.itemName = params.getItem_name();
        this.quantity = params.getQuantity();
        this.totalAmount = params.getTotal_amount();
        this.valAmount = params.getVal_amount();
        this.taxFreeAmount = params.getTax_free_amount();
        this.approvalUrl = params.getApproval_url();
        this.failUrl = params.getFail_url();
        this.cancelUrl = params.getCancel_url();
    }

    /************************** 주문 내역 정보 **************************/

    @Builder
    public Reserve(Long id,
                int capacity,
                LocalDateTime startTime,
                LocalDateTime endTime,
                Long placeId,
                Long memberId,
                Long totalCharge) {
        this.id = id;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.placeId = placeId;
        this.memberId = memberId;
        this.totalCharge = totalCharge;
    }

    public void editReserve(int capacity,
                            LocalDateTime startTime,
                            LocalDateTime endTime){
        this.capacity = capacity;
        this. startTime = startTime;
        this. endTime = endTime;
    }

    public void setStatus(ReserveStatus reserveStatus) {
        this.status = reserveStatus;
    }

    public enum ReserveStatus {
        PAY_IN_PROGRESS(1, "결제 대기중"),
        PAY_SUCCESS(2, "결제 완료"),
        PAY_FAILED(3, "결제 실패"),
        PAY_CANCELED(4, "결제 취소"),
        RESERVATION_CANCELED(5, "예약 취소"),
        CHECKOUT(6, "체크아웃");

        @Getter
        private int stepNumber;

        @Getter
        private String status;

        ReserveStatus(int stepNumber, String status) {
            this.stepNumber = stepNumber;
            this.status = status;
        }
    }
}
