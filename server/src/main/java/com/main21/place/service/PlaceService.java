package com.main21.place.service;

import com.main21.place.dto.PlaceDto;
import com.main21.place.entity.Place;
import com.main21.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    /*
     * 공간 등록 메서드
     * @param place
     * @return
     */
    public Place createPlace(Place place) {
        return placeRepository.save(place);
    }


}
