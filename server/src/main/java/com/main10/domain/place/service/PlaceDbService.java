package com.main10.domain.place.service;

import com.main10.domain.place.repository.PlaceCategoryRepository;
import com.main10.domain.place.repository.PlaceImageRepository;
import com.main10.domain.place.repository.PlaceRepository;
import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import com.main10.domain.place.dto.PlaceCategoryDto;
import com.main10.domain.place.dto.PlaceDto;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.entity.PlaceImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceDbService {

    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final PlaceCategoryRepository placeCategoryRepository;

    /**
     * 공간 정보 조회 메서드
     *
     * @param placeId 장소 식별자
     * @return Place
     * @author mozzi327
     */
    public Place ifExistsReturnPlace(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));
    }

    /**
     * 공간 저장 메서드
     * @param place 저장할 장소 정보
     * @author LimJaeMinZ
     */
    public void savePlace(Place place) {
        placeRepository.save(place);
    }

    /**
     * 공간 이미지 저장 메서드
     *
     * @param placeImage 저장할 장소 이미지 정보
     * @return PlaceImage
     * @author LimJaeMinZ
     */
    public PlaceImage savePlaceImage(PlaceImage placeImage) {
        return placeImageRepository.save(placeImage);
    }

    /**
     * 공간 삭제 메서드
     *
     * @param place 삭제할 장소 이미지 정보
     * @author quartz614
     */
    public void deletePlace(Place place) {
        placeRepository.delete(place);
    }

    /**
     * 공간 카테고리 삭제 메서드
     *
     * @param placeId 장소 식별자
     * @author LeeGoh
     */
    public void deletePlaceCategory(Long placeId) {
        placeCategoryRepository.deleteAllByPlaceId(placeId);
    }


    /**
     * Pagination 적용한 공간 전체 조회 메서드
     *
     * @param pageable 페이지 정보
     * @return Page(PlaceDto.Response)
     * @author LeeGoh
     */
    public Page<PlaceDto.Response> getPlacesPage(Pageable pageable) {
        return placeRepository.getPlacesPage(pageable);
    }

    /**
     * 공간 전체 조회 테스트(이미지 limit)
     * @param pageable 페이지 정보
     * @return Page(PlaceDto.ResponseTest)
     * @author LimJaeMinZ
     */
    public Page<PlaceDto.ResponseTest> getPlacesPageTest(Pageable pageable) {
       List<Place> placeList = placeRepository.findAll();
       List<PlaceDto.ResponseTest> responseTestList = new ArrayList<>();
       placeList.forEach(
               place -> {
                   PlaceDto.ResponseTest responseTest = PlaceDto.ResponseTest.builder()
                           .place(place)
                           .placeImage(placeImageRepository.findFirstByPlaceId(place.getId()))
                           .build();
                   responseTestList.add(responseTest);
               }
       );
       final int start = (int)pageable.getOffset();
       final int end = Math.min((start + pageable.getPageSize()), responseTestList.size());

        return new PageImpl<>(responseTestList.subList(start, end), pageable, responseTestList.size());
    }

    /**
     * Slice 무한스크롤 공간 전체 조회 메서드
     * @param pageable
     * @return
     @author LeeGoh
     */
    /*
    @Transactional
    public Slice<PlaceDto.Response> getPlacesSlice(Pageable pageable) {
        return placeRepository.getPlacesSlice(pageable);
    }
     */

    /**
     * Pagination 적용한 카테고리별 공간 조회 메서드
     *
     * @param categoryId 카테고리 식별자
     * @param pageable 페이지 정보
     * @return Page(PlaceCategoryDto.Response)
     * @author LeeGoh
     */
    public Page<PlaceCategoryDto.Response> getCategoryPage(Long categoryId, Pageable pageable) {
        return placeRepository.getCategoryPage(categoryId, pageable);
    }

    /**
     * 카테고리별 공간 조회 테스트(이미지 limit)
     * @param categoryId 카테고리 식별자
     * @param pageable 페이지 정보
     * @return Page(PlaceCategoryDto.ResponseTest)
     * @author LimJaeMinZ
     */
    public Page<PlaceCategoryDto.ResponseTest> getCategoryPageTest(Long categoryId, Pageable pageable) {
        List<Place> placeList = placeRepository.getCategoryPageTest(categoryId);
        List<PlaceCategoryDto.ResponseTest> responseTestList = new ArrayList<>();
        placeList.forEach(
                place -> {
                    PlaceCategoryDto.ResponseTest responseTest = PlaceCategoryDto.ResponseTest.builder()
                            .categoryId(categoryId)
                            .place(place)
                            .placeImage(placeImageRepository.findFirstByPlaceId(place.getId()))
                            .build();
                    responseTestList.add(responseTest);
                }
        );
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), responseTestList.size());

        return new PageImpl<>(responseTestList.subList(start, end), pageable, responseTestList.size());
    }

    /**
     * Slice 무한스크롤 카테고리별 공간 조회 메서드
     * @param categoryId
     * @param pageable
     * @return
     @author LeeGoh
     */
    /*
    @Transactional
    public Slice<PlaceCategoryDto.Response> getCategorySlice(Long categoryId, Pageable pageable) {
        return placeRepository.getCategorySlice(categoryId, pageable);
    }
     */

    /**
     * 공간 마이페이지 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page(PlaceDto.Response)
     * @author Quartz614
     */
    @Transactional
    public Page<PlaceDto.Response> getPlaceMypage(Long memberId, Pageable pageable) {
        return placeRepository.getPlaceMypage(memberId, pageable);
    }

    /**
     * 공간 최소 가격, 최대 가격, 인원수별 상세 검색 메서드
     *
     * @param searchDetail 상세 검색 정보
     * @param pageable 페이지 정보
     * @return Page(PlaceDto.Response)
     * @author LeeGoh
     */
    public Page<PlaceDto.Response> searchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable) {
        return placeRepository.searchDetail(searchDetail, pageable);
    }

    /**
     * 공간 전체 타이틀 검색 메서드
     *
     * @param title 장소 제목
     * @param pageable 페이지 정보
     * @return Page(PlaceDto.Response)
     * @author LeeGoh
     */
    public Page<PlaceDto.Response> searchTitleAll(String title, Pageable pageable) {
        return placeRepository.searchTitle(split(title), pageable);
    }

    /**
     * 공간 카테고리별 타이틀 검색 메서드
     *
     * @param categoryId 카테고리 식별자
     * @param title 장소 제목
     * @param pageable 페이지 정보
     * @return Page(PlaceCategoryDto.Response)
     * @author LeeGoh
     */
    public Page<PlaceCategoryDto.Response> searchTitleCategory(Long categoryId, String title, Pageable pageable) {
        return placeRepository.searchCategoryTitle(categoryId, split(title), pageable);
    }

    /**
     * Stirng 검색어 공백을 기준으로 분리하여 List에 담는 메서드
     *
     * @param title 장소 제목
     * @return List(String)
     * @author LeeGoh
     */
    public List<String> split(String title) {
        List<String> titles = new ArrayList<>();
        String[] list = title.split(" ");
        Collections.addAll(titles, list);
        return titles;
    }
}
