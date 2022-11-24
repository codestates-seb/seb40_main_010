package com.main21.reserve.service;

import com.main21.reserve.entity.CancelReason;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.repository.CancelReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelReasonService {

    private CancelReasonRepository cancelReasonRepository;

    /**
     * 예약 취소 사유 입력 메서드
     * 사용 미정
     *
     * @param cancel 취소 사유
     */
    public void cancelReason(String cancel, Reserve reserve) {
        // 입력받은 취소 사유와 예약 데이터 저장
        CancelReason cancelReason = CancelReason.builder()
                .reason(cancel)
                .reserveId(reserve.getId())
                .memberId(reserve.getMemberId())
                .build();

        cancelReasonRepository.save(cancelReason);
    }
}
