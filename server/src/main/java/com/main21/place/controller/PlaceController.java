package com.main21.place.controller;

import com.main21.dto.MultiResponseDto;
import com.main21.file.S3Upload;
import com.main21.place.dto.*;

import com.main21.place.entity.Place;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceImageService placeImageService;
    private final PlaceCategoryService placeCategoryService;

    /**
     * 장소 + S3이미지 업로드
     */
    @PostMapping(value = "/place/postS3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createS3(HttpServletRequest request,
                         @RequestPart(value = "key") PlacePostDto placePostDto,
                         @RequestPart(value = "file") List<MultipartFile> files) throws Exception {

        PlacePostDto postDto =
                PlacePostDto.builder()
                        .title(placePostDto.getTitle())
                        .categoryList(placePostDto.getCategoryList())
                        .maxCapacity(placePostDto.getMaxCapacity())
                        .address(placePostDto.getAddress())
                        .detailInfo(placePostDto.getDetailInfo())
                        .charge(placePostDto.getCharge())
                        .build();

        placeService.createS3(placePostDto, files);
    }

    /**
     * 장소 생성 (Local)
     */
    @PostMapping(value = "/place/post",consumes = {"multipart/form-data"})
    public void createPlace(@RequestPart(value = "key") PlacePostDto placePostDto,
                            @RequestPart(value = "file") List<MultipartFile> files) throws Exception {

        PlacePostDto postDto =
                PlacePostDto.builder()
                        .title(placePostDto.getTitle())
                        .categoryList(placePostDto.getCategoryList())
                        .maxCapacity(placePostDto.getMaxCapacity())
                        .address(placePostDto.getAddress())
                        .detailInfo(placePostDto.getDetailInfo())
                        .charge(placePostDto.getCharge())
                        .build();


        placeService.createPlace(postDto, files);
    }

    /**
     * 장소 상세 조회
     */
    @GetMapping("/place/{place-id}")
    public PlaceResponseDto getPlace(@PathVariable("place-id") Long placeId) {

        List<PlaceImageResponseDto> placeImageResponseDtoList = placeImageService.findAllByPlaceImage(placeId);

        List<PlaceCategoryDto.Search> placeCategoryResponseDtoList = placeCategoryService.findByAllPlaceCategory(placeId);

        List<String> filePath = new ArrayList<>();
        for(PlaceImageResponseDto placeImageResponseDto : placeImageResponseDtoList)
            filePath.add(placeImageResponseDto.getFilePath());

        return placeService.searchPlace(placeId, filePath, placeCategoryResponseDtoList);
    }

    /**
     * Pagination 메인페이지 공간 전체 조회
     * @param memberId
     * @param pageable
     * @return
     */
    @GetMapping("/")
    public ResponseEntity getPlacesPage(@CookieValue(name = "memberId", required = false) Long memberId,
                                        Pageable pageable) {
        Page<PlaceDto.Response> pagePlace = placeService.getPlacesPage(pageable);
        List<PlaceDto.Response> place = pagePlace.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(place, pagePlace), HttpStatus.OK);
    }

    /**
     * Slice 무한스크롤 메인페이지 공간 전체 조회
     * @param memberId
     * @param pageable
     * @return
     */
    @GetMapping("/slice")
    public ResponseEntity getPlacesSlice(@CookieValue(name = "memberId", required = false) Long memberId,
                                         Pageable pageable) {
        Slice<PlaceDto.Response> place = placeService.getPlacesSlice(pageable);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }

    /**
     * 메인페이지 카테고리별 공간 조회
     * @param categoryId
     * @param pageable
     * @return
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
     */
    @GetMapping("slice/category/{category-id}")
    public ResponseEntity getCategorySlice(@PathVariable("category-id") Long categoryId,
                                           Pageable pageable) {

        Slice<PlaceCategoryDto.Response> place = placeService.getCategorySlice(categoryId, pageable);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }

    /**
     * 공간 상세 검색
     * @param searchDetail
     * @return
     */
    @PostMapping("/search/detail")
    public ResponseEntity searchDetail(@RequestBody PlaceDto.SearchDetail searchDetail,
                                       Pageable pageable) {

        Page<PlaceDto.Response> pagePlace = placeService.getSearchDetail(searchDetail, pageable);
        List<PlaceDto.Response> place = pagePlace.getContent();
        return new ResponseEntity(new MultiResponseDto<>(place, pagePlace), HttpStatus.OK);
    }
}
