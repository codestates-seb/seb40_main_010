package com.main21.place.controller;

import com.main21.dto.MultiResponseDto;
import com.main21.file.S3Upload;
import com.main21.place.dto.*;

import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceCategory;
import com.main21.place.entity.PlaceImage;
import com.main21.place.repository.PlaceCategoryRepository;
import com.main21.place.service.PlaceCategoryService;
import com.main21.place.service.PlaceImageService;
import com.main21.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.main21.member.utils.AuthConstant.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceImageService placeImageService;
    private final PlaceCategoryService placeCategoryService;

    /**
     * 장소 생성
     */
    @PostMapping(value = "/place/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createPlace(@RequestPart(value = "key") PlacePostDto placePostDto,
                         @RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                         @RequestPart(value = "file") List<MultipartFile> files) throws Exception {

        /** 로컬 환경 */
        placeService.createPlace(placePostDto,  refreshToken, files);

        /** S3 환경 */
        //placeService.createPlaceS3(placePostDto, refreshToken, files);
    }


    /**
     * 장소 상세 조회
     * @param placeId
     * @param refreshToken
     * @return
     */
    @GetMapping("/place/{place-id}")
    public PlaceResponseDto getPlace(@PathVariable("place-id") Long placeId,
                                     @RequestHeader(value = REFRESH_TOKEN, required = false) String refreshToken) {

        return placeService.searchPlace(placeId, refreshToken);
    }

    /**
     * 장소 수정
     */
    @PatchMapping(value = "/place/{place-id}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void patchPlace(@PathVariable("place-id") Long placeId,
                           @RequestPart(value = "key") PlacePatchDto placePatchDto,
                           @RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                           @RequestPart(value = "file") List<MultipartFile> files) throws Exception {
        /** 로컬 환경 */
        placeService.updatePlace(placeId, placePatchDto, refreshToken, files);

        /** S3 환경 */
        //placeService.updatePlaceS3(placeId, placePatchDto, refreshToken, files);
    }


    /**
     * Pagination 메인페이지 공간 전체 조회
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @GetMapping("/home")
    public ResponseEntity getPlacesPage(Pageable pageable) {
        Page<PlaceDto.Response> pagePlace = placeService.getPlacesPage(pageable);
        List<PlaceDto.Response> place = pagePlace.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(place, pagePlace), HttpStatus.OK);
    }

    /**
     * Slice 무한스크롤 메인페이지 공간 전체 조회
     * @param memberId
     * @param pageable
     * @return
     @author LeeGoh
     */
    /*
    @GetMapping("/slice")
    public ResponseEntity getPlacesSlice(@CookieValue(name = "memberId", required = false) Long memberId,
                                         Pageable pageable) {
        Slice<PlaceDto.Response> place = placeService.getPlacesSlice(pageable);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }
     */

    /**
     * 메인페이지 카테고리별 공간 조회
     * @param categoryId
     * @param pageable
     * @return
     @author LeeGoh
     */
    @GetMapping("category/{category-id}")
    public ResponseEntity getCategoryPage(@PathVariable("category-id") Long categoryId,
                                          Pageable pageable) {

        Page<PlaceCategoryDto.Response> pagePlace = placeService.getCategoryPage(categoryId, pageable);
        List<PlaceCategoryDto.Response> place = pagePlace.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(place, pagePlace), HttpStatus.OK);
    }

    /**
     * Slice 무한스크롤 메인페이지 카테고리별 공간 조회
     * @param categoryId
     * @param pageable
     * @return
     @author LeeGoh
     */
    /*
    @GetMapping("slice/category/{category-id}")
    public ResponseEntity getCategorySlice(@PathVariable("category-id") Long categoryId,
                                           Pageable pageable) {

        Slice<PlaceCategoryDto.Response> place = placeService.getCategorySlice(categoryId, pageable);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }
     */

    /**
     * 공간 최소 가격, 최대 가격, 인원수별 상세 검색
     * @param searchDetail
     * @return
     * @author LeeGoh
     */
    @PostMapping("/search/detail")
    public ResponseEntity searchDetail(@RequestBody PlaceDto.SearchDetail searchDetail,
                                       Pageable pageable) {

        Page<PlaceDto.Response> pagePlace = placeService.searchDetail(searchDetail, pageable);
        List<PlaceDto.Response> place = pagePlace.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(place, pagePlace), HttpStatus.OK);
    }

    /**
     * 공간 전체 타이틀 검색
     * @param title
     * @param pageable
     * @return
     @author LeeGoh
     */
    @GetMapping("/search/{title:.+}")
    public ResponseEntity searchTitleAll(@PathVariable("title") String title,
                                         Pageable pageable) {
        Page<PlaceDto.Response> pagePlace = placeService.searchTitleAll(title, pageable);
        List<PlaceDto.Response> place = pagePlace.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(place, pagePlace), HttpStatus.OK);
    }

    /**
     * 공간 카테고리별 타이틀 검색
     * @param categoryId
     * @param title
     * @param pageable
     * @return
     @author LeeGoh
     */
    @GetMapping("/category/{category-id}/search/{title:.+}")
    public ResponseEntity searchTitleCategory(@PathVariable("category-id") Long categoryId,
                                              @PathVariable("title") String title,
                                              Pageable pageable) {
        Page<PlaceCategoryDto.Response> pagePlace = placeService.searchTitleCategory(categoryId, title, pageable);
        List<PlaceCategoryDto.Response> place = pagePlace.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(place, pagePlace), HttpStatus.OK);
    }

    /**
     * 마이페이지 호스팅 조회
     * @param pageable
     * @return
     */
    @GetMapping("/place")
    public ResponseEntity getPlaceMypage(@RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                                         Pageable pageable) {
        Page<PlaceDto.Response> hosting = placeService.getPlaceMypage(refreshToken, pageable);
        List<PlaceDto.Response> place = hosting.getContent();
        return new ResponseEntity(new MultiResponseDto<>(place, hosting), HttpStatus.OK);
    }

    /**
     * 호스팅 삭제
     * @param refreshToken 리프래시 토큰
     * @param placeId 장소 식별자
     * @return
     */
    @DeleteMapping("/place/{place-id}")
    public ResponseEntity deleteHosting(@RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                                        @PathVariable("place-id") Long placeId) {
        placeService.deleteHosting(refreshToken, placeId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
