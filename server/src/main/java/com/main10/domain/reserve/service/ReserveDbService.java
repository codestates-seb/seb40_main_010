package com.main10.domain.reserve.service;

import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import com.main10.domain.reserve.dto.ReserveDto;
import com.main10.domain.reserve.entity.Reserve;
import com.main10.domain.reserve.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Reserve saveReserve(Reserve reserve) {
        return reserveRepository.save(reserve);
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

    /**
     * 예약 정보 전체 조회 메서드
     *
     * @param placeId
     * @return
     * @author LeeGoh
     */
    public List<Reserve> findAllByReserves(Long placeId) {
        return reserveRepository.findAllByPlaceIdAndStatus(placeId, Reserve.ReserveStatus.PAY_SUCCESS);
    }


    /**
     * 장소에 대한 예약 정보 전체 조회 메서드
     *
     * @param placeId 장소 식별자
     * @return List(ReserveDto.Detail)
     * @author mozzi327
     */
    public List<ReserveDto.Detail> findAllReserveForPlace(Long placeId) {
        return reserveRepository.getDetailReservationTime(placeId);
    }
}
