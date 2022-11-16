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
    public void createS3(PlacePostDto postDto, List<MultipartFile> files) throws Exception {
        //유저 확인 필요
        String dir = "placeImage";

        Place place = new Place(
                postDto.getTitle(),
                postDto.getDetailInfo(),
                postDto.getMaxCapacity(),
                postDto.getAddress(),
                postDto.getCharge()
        );

        List<PlaceImage> placeImageList = s3Upload.uploadList(files, dir);

        //파일이 존재할 때 처리
        if(!placeImageList.isEmpty()) {
            for(PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                place.addPlaceImage(placeImageRepository.save(placeImage));
            }
        }

        Long placeId = placeRepository.save(place).getId();

        List<String> categoryList = postDto.getCategoryList();
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
    public Long createPlace(PlacePostDto postDto, List<MultipartFile> files) throws Exception {
        //유저 확인 필요

        Place place = new Place(
                postDto.getTitle(),
                postDto.getDetailInfo(),
                postDto.getMaxCapacity(),
                postDto.getAddress(),
                postDto.getCharge()
        );

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

        List<String> categoryList = postDto.getCategoryList();
        categoryList.forEach(
                s -> {
                    Category category = categoryRepository.findByCategoryName(s);
                    PlaceCategory placeCategory = new PlaceCategory(place, s, category);
                    placeCategoryRepository.save(placeCategory);
                }
        );

        return placeId;
    }

    /**
     * 장소 상세검색
     */
    @Transactional
    public PlaceResponseDto searchPlace(Long placeId, List<String> filePath, List<PlaceCategoryDto.Search> placeCategoryResponseDtoList) { //List<Long> fileId) {

        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        return new PlaceResponseDto(place, filePath, placeCategoryResponseDtoList);
    }

    /**
     * Pagination 적용한 공간 전체 조회 메서드
     * @param pageable
     * @return
     */
    @Transactional
    public Page<PlaceDto.Response> getPlacesPage(Pageable pageable) {
        return placeRepository.getPlacesPage(pageable);
    }


    /**
     * Slice 무한스크롤 공간 전체 조회 메서드
     * @param pageable
     * @return
     */
    @Transactional
    public Slice<PlaceDto.Response> getPlacesSlice(Pageable pageable) {
        return placeRepository.getPlacesSlice(pageable);
    }

    /**
     * Pagination 적용한 카테고리별 공간 조회 메서드
     * @param categoryId
     * @param pageable
     * @return
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
     */
    @Transactional
    public Slice<PlaceCategoryDto.Response> getCategorySlice(Long categoryId, Pageable pageable) {
        return placeRepository.getCategorySlice(categoryId, pageable);
    }

    /**
     * Pagination 적용한 카테고리별 공간 검색 메서드
     * @param searchDetail
     * @param pageable
     * @return
     */
    @Transactional
    public Page<PlaceDto.Response> getSearchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable) {
        return placeRepository.getSearchDetail(searchDetail, pageable);
    }
}

