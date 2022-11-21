package com.main21.place.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.file.FileHandler;
import com.main21.file.S3Upload;
import com.main21.file.UploadFile;
import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import com.main21.place.dto.PlacePostDto;
import com.main21.place.dto.PlaceResponseDto;
import com.main21.place.entity.Category;
import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceCategory;
import com.main21.place.entity.PlaceImage;
import com.main21.place.repository.CategoryRepository;
import com.main21.place.repository.PlaceCategoryRepository;
import com.main21.place.repository.PlaceImageRepository;
import com.main21.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final FileHandler fileHandler;
    private final CategoryRepository categoryRepository;
    private final PlaceCategoryRepository placeCategoryRepository;

    @Autowired
    private S3Upload s3Upload;

    /**
     * 장소 + S3이미지 저장 메서드
     */
    @Transactional
    public void createPlaceS3(PlacePostDto placePostDto, List<MultipartFile> files) throws Exception {
        //유저 확인 필요
        String dir = "placeImage";

        Place place = Place.builder()
                .title(placePostDto.getTitle())
                .detailInfo(placePostDto.getDetailInfo())
                .maxCapacity(placePostDto.getMaxCapacity())
                .maxSpace(placePostDto.getMaxSpace())
                .address(placePostDto.getAddress())
                .charge(placePostDto.getCharge())
                .memberId(placePostDto.getMemberId())
                .score(placePostDto.getScore())
                .view(placePostDto.getView())
                .build();


        List<PlaceImage> placeImageList = s3Upload.uploadList(files, dir);

        //파일이 존재할 때 처리
        if(!placeImageList.isEmpty()) {
            for(PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                place.addPlaceImage(placeImageRepository.save(placeImage));
            }
        }

        Long placeId = placeRepository.save(place).getId();

        List<String> categoryList = placePostDto.getCategoryList();
        categoryList.forEach(
                s -> {
                    Category category = categoryRepository.findByCategoryName(s);
                    PlaceCategory placeCategory = new PlaceCategory(place, s, category);
                    placeCategoryRepository.save(placeCategory);
                }
        );
    }

    /**
     * 장소 저장 메서드 (Local)
     */
    @Transactional
    public void createPlace(PlacePostDto placePostDto, List<MultipartFile> files, Long memberId) throws Exception {
        //유저 확인 필요

        Place place = Place.builder()
                .title(placePostDto.getTitle())
                .detailInfo(placePostDto.getDetailInfo())
                .maxCapacity(placePostDto.getMaxCapacity())
                .maxSpace(placePostDto.getMaxSpace())
                .address(placePostDto.getAddress())
                .charge(placePostDto.getCharge())
                .memberId(memberId)
                .score(placePostDto.getScore())
                .view(placePostDto.getView())
                .build();

        List<UploadFile> uploadFileList = fileHandler.parseUploadFileInfo(files);
        List<PlaceImage> placeImageList = new ArrayList<>();

        uploadFileList.forEach(uploadFile -> {
            PlaceImage placeImage = PlaceImage.builder()
                    .originFileName(uploadFile.getOriginFileName())
                    .fileName(uploadFile.getFileName())
                    .filePath(uploadFile.getFilePath())
                    .fileSize(uploadFile.getFileSize())
                    .build();
            placeImageList.add(placeImage);
        });

        //파일이 존재할 때 처리
        if(!placeImageList.isEmpty()) {
            for(PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                place.addPlaceImage(placeImageRepository.save(placeImage));
            }
        }

        Long placeId = placeRepository.save(place).getId();

        List<String> categoryList = placePostDto.getCategoryList();
        categoryList.forEach(
                s -> {
                    Category category = categoryRepository.findByCategoryName(s);
                    PlaceCategory placeCategory = new PlaceCategory(place, s, category);
                    placeCategoryRepository.save(placeCategory);
                }
        );
    }

    /**
     * 장소 상세검색
     */
    @Transactional
    public PlaceResponseDto searchPlace(Long placeId, List<String> filePath, List<String> categoryList) {

        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        return new PlaceResponseDto(place, filePath, categoryList);
    }

    /**
     * Pagination 적용한 공간 전체 조회 메서드
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @Transactional
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
     * @param categoryId
     * @param pageable
     * @return
      @author LeeGoh
     */
    @Transactional
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
     * 공간 최소 가격, 최대 가격, 인원수별 상세 검색 메서드
     * @param searchDetail
     * @param pageable
     * @return
      @author LeeGoh
     */
    @Transactional
    public Page<PlaceDto.Response> searchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable) {
        return placeRepository.searchDetail(searchDetail, pageable);
    }

    /**
     * 공간 전체 타이틀 검색 메서드
     * @param title
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @Transactional
    public Page<PlaceDto.Response> searchTitleAll(String title, Pageable pageable) {
        return placeRepository.searchTitle(split(title), pageable);
    }

    /**
     * 공간 카테고리별 타이틀 검색 메서드
     * @param categoryId
     * @param title
     * @param pageable
     * @return
     @author LeeGoh
     */
    @Transactional
    public Page<PlaceCategoryDto.Response> searchTitleCategory(Long categoryId, String title, Pageable pageable) {
        return placeRepository.searchCategoryTitle(categoryId, split(title), pageable);
    }

    /**
     * Stirng 검색어 공백을 기준으로 분리하여 List에 담는 메서드
     * @param title
     * @return
     @author LeeGoh
     */
    public List<String> split(String title) {
        List<String> titles = new ArrayList<>();
        String[] list = title.split(" ");
        for (int i = 0; i < list.length; i++) {
            titles.add(list[i]);
        }
        return titles;
    }

    /** getPlaceMypage
     * @param pageable
     * @return
     * @author quartz614
     */
    @Transactional
    public Page<PlaceDto.Response> getPlaceMypage(Long memberId, Pageable pageable) {
        return placeRepository.getPlaceMypage(memberId, pageable);
    }
}

