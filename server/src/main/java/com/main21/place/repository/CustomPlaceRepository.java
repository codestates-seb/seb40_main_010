package com.main21.place.repository;

import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomPlaceRepository {
    Page<PlaceDto.Response> getPlacesPage(Pageable pageable);
//    Slice<PlaceDto.Response> getPlacesSlice(Pageable pageable);
    Page<PlaceCategoryDto.Response> getCategoryPage(Long categoryId, Pageable pageable);
//    Slice<PlaceCategoryDto.Response> getCategorySlice(Long categoryId, Pageable pageable);
    Page<PlaceDto.Response> searchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable);
    Page<PlaceDto.Response> searchTitle(List<String> titles, Pageable pageable);
    Page<PlaceCategoryDto.Response> searchCategoryTitle(Long categoryId, List<String> titles, Pageable pageable);

    Page<PlaceDto.Response> getPlaceMypage(Long memberId, Pageable pageable);
}
