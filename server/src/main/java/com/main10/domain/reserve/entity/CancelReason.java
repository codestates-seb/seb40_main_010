package com.main10.domain.reserve.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancelReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CANCEL_REASON_ID")
    private Long id;

    private String reason;

    private Long memberId;

    private Long reserveId;

    @Builder
    public CancelReason(String reason, Long memberId, Long reserveId) {
        this.reason = reason;
        this.memberId = memberId;
        this.reserveId = reserveId;
    }
}
