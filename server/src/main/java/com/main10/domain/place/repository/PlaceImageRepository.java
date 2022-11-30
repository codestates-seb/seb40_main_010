package com.main10.domain.place.repository;

import com.main10.domain.place.entity.PlaceImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {
    List<PlaceImage> findAllByPlaceId(Long placeId);

    PlaceImage findFirstByPlaceId(Long placeId);

}
