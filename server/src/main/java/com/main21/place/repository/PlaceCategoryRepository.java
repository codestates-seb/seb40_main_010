package com.main21.place.repository;

import com.main21.place.entity.PlaceCategory;
import com.main21.place.entity.PlaceImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
    List<PlaceCategory> findAllByPlaceId(Long placeId);
}
