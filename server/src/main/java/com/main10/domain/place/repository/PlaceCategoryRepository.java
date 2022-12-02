package com.main10.domain.place.repository;

import com.main10.domain.place.entity.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
    List<PlaceCategory> findAllByPlaceId(Long placeId);
    void deleteAllByPlaceId(Long placeId);
}
