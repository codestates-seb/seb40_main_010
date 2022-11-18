package com.main21.reserve.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.entity.Place;
import com.main21.place.repository.PlaceRepository;
import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final PlaceRepository placeRepository;


    /**
     * 예약 등록 메서드
     * @param post 예약 등록 정보
     * @param placeId 장소 식별자
     * @param memberId 사용자 식별자
     * @author LimJaeminZ
     */
    public void createReserve(ReserveDto.Post post, Long placeId, Long memberId) {

        //공간 확인
        Place findPlace = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        //유저확인
        Reserve reserve = Reserve.builder()
                .capacity(post.getCapacity())
                .startTime(post.getStartTime())
                .endTime(post.getEndTime())
                .placeId(findPlace.getId())
                .memberId(memberId)
                .totalCharge(((post.getEndTime().getTime() - post.getStartTime().getTime())/3600000) * findPlace.getCharge())
                .build();

        reserveRepository.save(reserve);
    }











    /* ------------------------------------ 일단 예외 -------------------------------------*/







    /**
     * 예약 수정 메서드
     * @param patch 에약 수정 정보
     * @param reserveId 예약 식별자
     * @param memberId 사용자 식별자
     * @author Quartz614
     */
    public void updateReserve(ReserveDto.Patch patch, Long reserveId, Long memberId) {
        Reserve findReserve = reserveRepository
                .findById(reserveId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND)); // 추후 수정
        if (!memberId.equals(findReserve.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        findReserve.editReserve(patch.getCapacity(), patch.getStartTime(), patch.getEndTime());
        reserveRepository.save(findReserve);
    }


    /**
     * 예약 전체 조회 메서드
     * @param memberId 사용자 식별자
     * @return List
     * @author LeeGoh
     */
    public Page<ReserveDto.Response> getReservation(Long memberId, Pageable pageable) {
        return reserveRepository.getReservation(memberId, pageable);
    }


    /**
     * 예약 삭제 메서드
     * @param reserveId 예약 식별자
     * @author LeeGoh
     */
    public void deleteReserve(Long reserveId) {
        reserveRepository.deleteById(reserveId);
    }
}
