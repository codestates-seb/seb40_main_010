package com.main10.domain.place.service;

import com.main10.domain.place.repository.CategoryRepository;
import com.main10.domain.place.repository.PlaceCategoryRepository;
import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import com.main10.domain.place.dto.PlaceCategoryDto;
import com.main10.domain.place.entity.Category;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.entity.PlaceCategory;
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

    /**
     * 장소별 카테고리 리스트 조회 메서드
     * @param placeId 장소 식별자
     * @return List(String)
     * @author LimJaeMinZ
     */
    public List<String>findByAllPlaceCategoryList(Long placeId) {
        List<PlaceCategory> placeCategoryList = placeCategoryRepository.findAllByPlaceId(placeId);

        return placeCategoryList.stream()
                .map(placeCategory -> placeCategory.getCategory().getCategoryName())
                .collect(Collectors.toList());
    }

    /**
     * 장소 카테고리 리스트 조회 메서드2(객체)
     * @param placeId 장소 식별자
     * @return List(PlaceCategory)
     * @author LimJaeMinZ
     */
    public List<PlaceCategory>findByAllPlaceCategoryList2(Long placeId) {
        return placeCategoryRepository.findAllByPlaceId(placeId);
    }

    /**
     * 장소별 카테고리 삭제 메서드
     * @param Id 장소 카테고리 식별자
     * @author LimJaeMinZ
     */
    public void deletePlaceCategory(Long Id) {
        placeCategoryRepository.deleteById(Id);
    }

    /**
     * 장소별 카테고리 조회 메서드
     * @param Id 장소 카테고리 식별자
     * @return PlaceCategoryDto.Search
     * @author LimJaeMinZ
     */
    public PlaceCategoryDto.Search findByPlaceCategoryId(Long Id) {

        PlaceCategory placeCategory = placeCategoryRepository.findById(Id).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));

        return PlaceCategoryDto.Search
                .builder()
                .categoryId(placeCategory.getId())
                .categoryName(placeCategory.getCategoryName())
                .build();
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
