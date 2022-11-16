package com.main21.place.service;

import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.entity.PlaceCategory;
import com.main21.place.repository.PlaceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceCategoryService {
    private final PlaceCategoryRepository placeCategoryRepository;

    public List<PlaceCategoryDto.Search> findByAllPlaceCategory(Long placeId) {

        List<PlaceCategory> placeCategoryList = placeCategoryRepository.findAllByPlaceId(placeId);

        return placeCategoryList.stream()
                .map(PlaceCategoryDto.Search::new)
                .collect(Collectors.toList());
    }
}
