package com.main21.reserve.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.entity.Place;
import com.main21.place.repository.PlaceRepository;
import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final PlaceRepository placeRepository;

    // 예약 등록
    public Reserve createReserve(ReserveDto.Post post, Long placeId, Long memberId) {

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

        Reserve savedReserve = reserveRepository.save(reserve);
        findPlace.addReserve(savedReserve.getPlaceId());

        return reserve;
    }

    // 예약 수정
    public Reserve updateReserve(ReserveDto.Patch patch, Long reserveId) {
        Reserve findReserve = reserveRepository.findById(reserveId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND)); // 추후 수정
        findReserve.editReserve(patch.getCapacity(), patch.getStartTime(), patch.getEndTime());

        Reserve updateReserve = reserveRepository.save(findReserve);

        return updateReserve;
    }

    // 예약 전체 조회
    public List<ReserveDto.Response> getReservation(Long memberId) {
        return reserveRepository.getReservation(memberId);
    }

    // 예약 삭제(취소)
    public void deleteReserve(Long reserveId) {
        reserveRepository.deleteById(reserveId);
    }

}
