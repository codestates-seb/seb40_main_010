package com.main10.domain.place.controller;

import com.main10.domain.place.dto.*;
import com.main10.domain.dto.MultiResponseDto;
import com.main10.domain.place.service.PlaceDbService;
import com.main10.domain.place.service.PlaceService;
import com.main10.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceDbService placeDbService;

    /**
     * 공간 생성 컨트롤러
     *
     * @param placePostDto 공간 등록 dto
     * @param authentication 사용자 인증 정보
     * @param files 이미지 파일
     * @throws Exception 예외
     * @author LimJaeminZ
     */
    @PostMapping(value = "/place/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPlace(@RequestPart(value = "key") @Valid PlaceDto.Create placePostDto,
                         Authentication authentication,
                         @RequestPart(value = "file") List<MultipartFile> files) throws Exception {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        /** 로컬 환경 */
//        placeService.createPlace(placePostDto,  token.getId(), files);

        /** S3 환경 */
        placeService.createPlaceS3(placePostDto, token.getId(), files);
        return ResponseEntity.ok().build();
    }

    /**
     * 공간 상세 조회 컨트롤러
     *
     * @param placeId 공간 식별자
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author LimJaeminZ
     */
    @GetMapping("/place/{place-id}")
    public ResponseEntity<PlaceDto.DetailResponse> getPlace(@PathVariable("place-id") Long placeId,
                                     Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId;
        if (token == null) memberId = null;
        else memberId = token.getId();
        return ResponseEntity.ok(placeService.searchPlace(placeId, memberId));
    }

    /**
     * 공간 수정 컨트롤러
     *
     * @param placeId 공간 식별자
     * @param placePatchDto 공간 수정 dto
     * @param authentication 사용자 인증 정보
     * @param files 이미지 파일
     * @throws Exception 예외
     * @author LimJaeminZ
     */
    @PostMapping(value = "/place/{place-id}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> patchPlace(@PathVariable("place-id") Long placeId,
                           @RequestPart(value = "key") @Valid PlaceDto.Update placePatchDto,
                           Authentication authentication,
                           @RequestPart(value = "file") List<MultipartFile> files) throws Exception {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        /** 로컬 환경 */
//        placeService.updatePlace(placeId, placePatchDto, token.getId(), files);

        /** S3 환경 */
        placeService.updatePlaceS3(placeId, placePatchDto, token.getId(), files);

        return ResponseEntity.ok().build();
    }


    /**
     * Pagination 메인페이지 공간 전체 조회 컨트롤
     *
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/home")
    public ResponseEntity<MultiResponseDto<?>> getPlacesPage(Pageable pageable) {
        Page<PlaceDto.Response> pagePlace = placeDbService.getPlacesPage(pageable);
        List<PlaceDto.Response> place = pagePlace.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(place, pagePlace));
    }

    /**
     * 공간 전체 조회 테스트(이미지 limit)
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LimJaeMinZ
     */
    @GetMapping("/home/test")
    public ResponseEntity<MultiResponseDto<?>> getPlacesPageTest(Pageable pageable) {
        Page<PlaceDto.ResponseTest> pagePlace = placeDbService.getPlacesPageTest(pageable);
        List<PlaceDto.ResponseTest> place = pagePlace.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(place, pagePlace));
    }

    /**
     * Slice 무한스크롤 메인페이지 공간 전체 조회 컨트롤
     *
     * @param refreshToken 리프래시 토큰
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    /*
    @GetMapping("/slice")
    public ResponseEntity getPlacesSlice(@RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                                         Pageable pageable) {
        Slice<PlaceDto.Response> place = placeDbService.getPlacesSlice(pageable);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }
     */

    /**
     * 메인페이지 카테고리별 공간 조회 컨트롤
     *
     * @param categoryId 카테고리 식별자
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("category/{category-id}")
    public ResponseEntity<MultiResponseDto<?>> getCategoryPage(@PathVariable("category-id") Long categoryId,
                                          Pageable pageable) {
        Page<PlaceCategoryDto.Response> pagePlace = placeDbService.getCategoryPage(categoryId, pageable);
        List<PlaceCategoryDto.Response> place = pagePlace.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(place, pagePlace));
    }

    /**
     * 카테고리별 공간 조회 테스트(이미지 limit)
     * @param categoryId 카테고리 식별자
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LimJaeMinZ
     */
    @GetMapping("category/{category-id}/test")
    public ResponseEntity<MultiResponseDto<?>> getCategoryPageTest(@PathVariable("category-id") Long categoryId,
                                          Pageable pageable) {
        Page<PlaceCategoryDto.ResponseTest> pagePlace = placeDbService.getCategoryPageTest(categoryId, pageable);
        List<PlaceCategoryDto.ResponseTest> place = pagePlace.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(place, pagePlace));
    }

    /**
     * Slice 무한스크롤 메인페이지 카테고리별 공간 조회 컨트롤
     *
     * @param categoryId 카테고리 식별자
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LeeGoh
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
     * 공간 최소 가격, 최대 가격, 인원수별 상세 검색 컨트롤
     *
     * @param searchDetail 상세 검색 dto
     * @return ResponseEntity
     * @author LeeGoh
     */
    @PostMapping("/search/detail")
    public ResponseEntity<MultiResponseDto<?>> searchDetail(@RequestBody @Valid PlaceDto.SearchDetail searchDetail,
                                       Pageable pageable) {

        Page<PlaceDto.Response> pagePlace = placeDbService.searchDetail(searchDetail, pageable);
        List<PlaceDto.Response> place = pagePlace.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(place, pagePlace));
    }

    /**
     * 공간 전체 타이틀 검색 컨트롤
     *
     * @param title 검색 키워드
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/search/{title:.+}")
    public ResponseEntity<MultiResponseDto<?>> searchTitleAll(@PathVariable("title") String title,
                                         Pageable pageable) {
        Page<PlaceDto.Response> pagePlace = placeDbService.searchTitleAll(title, pageable);
        List<PlaceDto.Response> place = pagePlace.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(place, pagePlace));
    }

    /**
     * 공간 카테고리별 타이틀 검색 컨트롤
     *
     * @param categoryId 카테고리 식별자
     * @param title 검색 키워드
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/category/{category-id}/search/{title:.+}")
    public ResponseEntity<MultiResponseDto<?>> searchTitleCategory(@PathVariable("category-id") Long categoryId,
                                              @PathVariable("title") String title,
                                              Pageable pageable) {
        Page<PlaceCategoryDto.Response> pagePlace = placeDbService.searchTitleCategory(categoryId, title, pageable);
        List<PlaceCategoryDto.Response> place = pagePlace.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(place, pagePlace));
    }

    /**
     * 마이페이지 호스팅 조회 컨트롤
     *
     * @param pageable 페이지 정보
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @GetMapping("/place")
    public ResponseEntity<MultiResponseDto<?>> getPlaceMypage(Authentication authentication,
                                         Pageable pageable) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Page<PlaceDto.Response> hosting = placeDbService.getPlaceMypage(token.getId(), pageable);
        List<PlaceDto.Response> place = hosting.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(place, hosting));
    }

    /**
     * 호스팅 삭제 컨트롤러
     *
     * @param authentication 사용자 인증 정보
     * @param placeId 공간 식별자
     * @return ResponseEntity
     * @author Quartz614
     */
    @DeleteMapping("/place/{place-id}")
    public ResponseEntity<?> deleteHosting(Authentication authentication,
                                        @PathVariable("place-id") Long placeId) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        placeService.deleteHosting(token.getId(), placeId);
        return ResponseEntity.ok().build();
    }
}
