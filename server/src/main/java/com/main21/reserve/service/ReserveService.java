package com.main21.reserve.service;

import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.entity.Reserve;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveService {
    //private final PlaceRepository placeRepository; 장소 리포지토리 생성 시 확인

    public Reserve createReserve(ReserveDto.Post post, Long placeId) {

        //Place findPlace = placeRepository.findPlaceById(placeId).orElseThrow()

        //Reserve reserve = new Reserve()

        return null;
    }
}
