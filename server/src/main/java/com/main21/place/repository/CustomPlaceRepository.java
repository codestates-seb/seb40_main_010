package com.main21.place.repository;

import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPlaceRepository {
    Page<PlaceDto.Response> getPlaces(Pageable pageable);
    Page<PlaceCategoryDto.Response> getCategory(Long categoryId, Pageable pageable);
}
