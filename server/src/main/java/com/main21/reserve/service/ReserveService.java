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

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final PlaceRepository placeRepository;

    public Reserve createReserve(ReserveDto.Post post, Long placeId) {

        //공간 확인
        Place findPlace = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        //유저확인

        Reserve reserve = new Reserve(post.getCapacity(), post.getCheckIn(), post.getCheckOut());

        //매핑 관계 저장

        Reserve savedReserve = reserveRepository.save(reserve);

        return null;
    }
}
