package com.main21.reserve.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveDbService {

    private final ReserveRepository reserveRepository;

    /**
     * 예약 정보 조회 메서드
     *
     * @param reserveId 예약 식별자
     * @return Reserve
     * @author mozzi327
     */
    public Reserve ifExistsReturnReserve(Long reserveId) {
        return reserveRepository.findById(reserveId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND));
    }

    /**
     * 예약 정보 저장 메서드
     *
     * @param reserve
     * @author LeeGoh
     */
    public void saveReserve(Reserve reserve) {
        reserveRepository.save(reserve);
    }

    /**
     * 예약 정보 조회 메서드
     *
     * @param memberId
     * @param pageable
     * @return
     * @author LeeGoh
     */
    public Page<ReserveDto.Response> getReservation(Long memberId, Pageable pageable) {
        return reserveRepository.getReservation(memberId, pageable);
    }
}
