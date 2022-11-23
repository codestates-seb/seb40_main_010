package com.main21.place.service;

import com.main21.place.dto.PlaceImageDto;
import com.main21.place.dto.PlaceImageResponseDto;
import com.main21.place.entity.PlaceImage;
import com.main21.place.repository.PlaceImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceImageService {
    private final PlaceImageRepository placeImageRepository;

    /**
     * 이미지 개별 조회
     */
    public PlaceImageDto findByFiledId(Long id) {

        PlaceImage placeImage = placeImageRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("file not found"));

        PlaceImageDto placeImageDto = PlaceImageDto.builder()
                .fileName(placeImage.getFileName())
                .filePath(placeImage.getFilePath())
                .fileSize(placeImage.getFileSize())
                .build();

        return placeImageDto;
    }

    /**
     * 이미지 전체 조회
     * */
    public List<PlaceImageResponseDto> findAllByPlaceImagePath(Long placeId) {

        List<PlaceImage> placeImageList = placeImageRepository.findAllByPlaceId(placeId);

        return placeImageList.stream()
                .map(PlaceImageResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PlaceImage> findAllByPlaceImage(Long placeId) {

        List<PlaceImage> placeImageList = placeImageRepository.findAllByPlaceId(placeId);

        return placeImageList;
    }

    /**
     * 이미지 삭제
     */
    public void delete(Long Id) {
        placeImageRepository.deleteById(Id);
    }
}
