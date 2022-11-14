package com.main21.place.service;

import com.main21.place.dto.PlaceDto;
import com.main21.place.entity.Place;
import com.main21.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    /*
     * 공간 등록 메서드
     * @param place
     * @return
     */
    public Place createPlace(PlaceDto.Post post) {

        Place place = Place.builder()
                .title(post.getTitle())
                .detailInfo(post.getDetailInfo())
                .maxCapacity(post.getMaxCapacity())
                .address(post.getAddress())
                .charge(post.getCharge())
                .build();

<<<<<<< Updated upstream
        return placeRepository.save(place);
=======
        //파일이 존재할 때 처리
        if(!placeImageList.isEmpty()) {
            for(PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                place.addPlaceImage(placeImageRepository.save(placeImage));
            }
        }
        return placeRepository.save(place).getId();
>>>>>>> Stashed changes
    }


}
