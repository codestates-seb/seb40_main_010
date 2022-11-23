package com.main21.place.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.entity.Category;
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

    public List<String>findByAllPlaceCategoryList(Long placeId) {

        List<PlaceCategory> placeCategoryList = placeCategoryRepository.findAllByPlaceId(placeId);

        return placeCategoryList.stream()
                .map(placeCategory -> placeCategory.getCategory().getCategoryName())
                .collect(Collectors.toList());
    }

    public List<PlaceCategory>findByAllPlaceCategoryList2(Long placeId) {

        List<PlaceCategory> placeCategoryList = placeCategoryRepository.findAllByPlaceId(placeId);

        return placeCategoryList;
    }

    public void deletePlaceCategory(Long Id) {

        placeCategoryRepository.deleteById(Id);
    }

    public PlaceCategoryDto.Search findByPlaceCategoryId(Long Id) {

        PlaceCategory placeCategory = placeCategoryRepository.findById(Id).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));

        PlaceCategoryDto.Search placeCategoryDto = PlaceCategoryDto.Search
                .builder()
                .categoryId(placeCategory.getId())
                .categoryName(placeCategory.getCategoryName())
                .build();


        return placeCategoryDto;
    }
}
