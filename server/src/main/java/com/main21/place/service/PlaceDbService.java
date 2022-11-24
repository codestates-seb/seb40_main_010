package com.main21.place.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceImage;
import com.main21.place.repository.PlaceCategoryRepository;
import com.main21.place.repository.PlaceImageRepository;
import com.main21.place.repository.PlaceRepository;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceDbService {

    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final PlaceCategoryRepository placeCategoryRepository;
    private final RedisUtils redisUtils;

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
     * @param place
     * @author
     */
    public void savePlace(Place place) {
        placeRepository.save(place);
    }

    /**
     * 공간 이미지 저장 메서드
     *
     * @param placeImage
     * @return
     * @author
     */
    public PlaceImage savePlaceImage(PlaceImage placeImage) {
        return placeImageRepository.save(placeImage);
    }

    /**
     * 공간 삭제 메서드
     *
     * @param place
     * @author quartz614
     */
    public void deletePlace(Place place) {
        placeRepository.delete(place);
    }

    /**
     * 공간 카테고리 삭제 메서드
     *
     * @param placeId
     * @author LeeGoh
     */
    public void deletePlaceCategory(Long placeId) {
        placeCategoryRepository.deleteAllByPlaceId(placeId);
    }


    /**
     * Pagination 적용한 공간 전체 조회 메서드
     *
     * @param pageable
     * @return
     * @author LeeGoh
     */
    public Page<PlaceDto.Response> getPlacesPage(Pageable pageable) {
        return placeRepository.getPlacesPage(pageable);
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
     * @param categoryId
     * @param pageable
     * @return
     * @author LeeGoh
     */
    public Page<PlaceCategoryDto.Response> getCategoryPage(Long categoryId, Pageable pageable) {
        return placeRepository.getCategoryPage(categoryId, pageable);
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
     * @param refreshToken
     * @param pageable
     * @return
     * @author quartz614
     */
    @Transactional
    public Page<PlaceDto.Response> getPlaceMypage(String refreshToken, Pageable pageable) {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);
        return placeRepository.getPlaceMypage(memberId, pageable);
    }

    /**
     * 공간 최소 가격, 최대 가격, 인원수별 상세 검색 메서드
     *
     * @param searchDetail
     * @param pageable
     * @return
     * @author LeeGoh
     */
    public Page<PlaceDto.Response> searchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable) {
        return placeRepository.searchDetail(searchDetail, pageable);
    }

    /**
     * 공간 전체 타이틀 검색 메서드
     *
     * @param title
     * @param pageable
     * @return
     * @author LeeGoh
     */
    public Page<PlaceDto.Response> searchTitleAll(String title, Pageable pageable) {
        return placeRepository.searchTitle(split(title), pageable);
    }

    /**
     * 공간 카테고리별 타이틀 검색 메서드
     *
     * @param categoryId
     * @param title
     * @param pageable
     * @return
     * @author LeeGoh
     */
    public Page<PlaceCategoryDto.Response> searchTitleCategory(Long categoryId, String title, Pageable pageable) {
        return placeRepository.searchCategoryTitle(categoryId, split(title), pageable);
    }

    /**
     * Stirng 검색어 공백을 기준으로 분리하여 List에 담는 메서드
     *
     * @param title
     * @return
     * @author LeeGoh
     */
    public List<String> split(String title) {
        List<String> titles = new ArrayList<>();
        String[] list = title.split(" ");
        for (int i = 0; i < list.length; i++) {
            titles.add(list[i]);
        }
        return titles;
    }
}
