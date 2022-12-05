package com.main10.domain.place.repository;

import com.main10.domain.place.dto.PlaceCategoryDto;
import com.main10.domain.place.dto.PlaceDto;
import com.main10.domain.place.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPlaceRepository {
    Page<PlaceDto.Response> getPlacesPage(Pageable pageable);
//    Slice<PlaceDto.Response> getPlacesSlice(Pageable pageable);

    PlaceDto.Response getPlace(Long placeId);
    Page<PlaceCategoryDto.Response> getCategoryPage(Long categoryId, Pageable pageable);
//    Slice<PlaceCategoryDto.Response> getCategorySlice(Long categoryId, Pageable pageable);
    Page<PlaceDto.Response> searchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable);
    Page<PlaceDto.Response> searchTitle(List<String> titles, Pageable pageable);
    Page<PlaceCategoryDto.Response> searchCategoryTitle(Long categoryId, List<String> titles, Pageable pageable);

    Page<PlaceDto.Response> getPlaceMypage(Long memberId, Pageable pageable);

    List<Place> getCategoryPageTest(Long categoryId);
}
