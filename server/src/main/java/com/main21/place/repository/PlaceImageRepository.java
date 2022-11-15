package com.main21.place.repository;

import com.main21.place.dto.PlaceImageResponseDto;
import com.main21.place.entity.PlaceImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {
    List<PlaceImage> findAllByPlaceId(Long placeId);

}
