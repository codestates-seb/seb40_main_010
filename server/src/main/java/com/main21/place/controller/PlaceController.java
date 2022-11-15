package com.main21.place.controller;

import com.main21.file.S3Upload;
import com.main21.place.dto.PlaceImageDto;
import com.main21.place.dto.PlaceImageResponseDto;
import com.main21.place.dto.PlacePostDto;

import com.main21.place.dto.PlaceResponseDto;
import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceImage;
import com.main21.place.service.PlaceImageService;
import com.main21.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
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
    private final S3Upload s3Upload;

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
     * 장소 생성
     */
    @PostMapping(value = "/place/post",consumes = {"multipart/form-data"})
    public void create(@RequestPart(value = "key") PlacePostDto placePostDto,
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

        placeService.create(postDto, files);
    }

    /**
     * 장소 상세검색(카테고리 출력 구현 필요)
     */
    @GetMapping("/place/{place-id}")
    public PlaceResponseDto getPlace(@PathVariable("place-id") Long placeId) {

        List<PlaceImageResponseDto> placeImageResponseDtoList = placeImageService.findAllByPlaceImage(placeId);

        List<String> filePath = new ArrayList<>();
        for(PlaceImageResponseDto placeImageResponseDto : placeImageResponseDtoList)
            filePath.add(placeImageResponseDto.getFilePath());

        return placeService.searchById(placeId, filePath);
    }
}
