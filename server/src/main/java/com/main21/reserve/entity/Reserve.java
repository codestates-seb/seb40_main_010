package com.main21.reserve.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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


    @Builder
    public Reserve(int capacity,
                   LocalDateTime startTime,
                   LocalDateTime endTime,
                   Long placeId,
                   Long memberId,
                   Long totalCharge) {
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
