package com.main21.place.service;

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
}
