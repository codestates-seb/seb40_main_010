package com.main21.place.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlacePostDto;
import com.main21.place.entity.Category;
import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceCategory;
import com.main21.place.repository.CategoryRepository;
import com.main21.place.repository.PlaceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceCategoryService {
    private final PlaceCategoryRepository placeCategoryRepository;
    private final CategoryRepository categoryRepository;

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

    /**
     * 수정된 카테고리 목록 전달 메서드
     * @param placeId
     * @param dbCategoryList db에 저장되어 있는 카테고리 목록
     * @param categoryList 전달받은 카테고리 목록
     * @return
     */
    public List<String> getAddCategoryList(Long placeId, List<PlaceCategory> dbCategoryList, List<String> categoryList) {
        // 새롭게 전달되어 온 카테고리를 저장할 List
        List<String> addCategoryList = new ArrayList<>();

        // 카테고리 수정
        if (!CollectionUtils.isEmpty(categoryList)) {
            List<String> categoryNameList = new ArrayList<>();

            for (PlaceCategory placeCategory : dbCategoryList) {
                PlaceCategoryDto.Search placeCategoryDto = findByPlaceCategoryId(placeCategory.getId());
                String categoryName = placeCategoryDto.getCategoryName();

                if (!categoryList.contains(categoryName)) {
                    deletePlaceCategory(placeCategory.getId());
                } else {
                    categoryNameList.add(categoryName);
                }
            }

            for (String s : categoryList) {
                if (!categoryNameList.contains(s)) {
                    addCategoryList.add(s);
                }
            }
        }
        return addCategoryList;
    }

    /**
     * 카테고리 목록 저장 메서드
     * @param categoryList 전달받은 카테고리 목록
     * @param place
     */
    public void saveCategoryList(List<String> categoryList, Place place) {
        categoryList.forEach(
                s -> {
                    Category category = categoryRepository.findByCategoryName(s);
                    PlaceCategory placeCategory = new PlaceCategory(place, s, category);
                    placeCategoryRepository.save(placeCategory);
                }
        );
    }
}
