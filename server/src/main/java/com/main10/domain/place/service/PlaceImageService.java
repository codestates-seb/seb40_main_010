package com.main10.domain.place.service;

import com.main10.domain.place.repository.PlaceImageRepository;
import com.main10.domain.place.dto.PlaceImageDto;
import com.main10.domain.place.entity.PlaceImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceImageService {
    private final PlaceImageRepository placeImageRepository;

    /**
     * 이미지 개별 조회 메서드
     * @param id 장소 이미지 식별자
     * @return PlaceImageDto
     * @author LimJaeMinZ
     */
    public PlaceImageDto findByFieldId(Long id) {

        PlaceImage placeImage = placeImageRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("file not found"));

        return PlaceImageDto.builder()
                .fileName(placeImage.getFileName())
                .filePath(placeImage.getFilePath())
                .fileSize(placeImage.getFileSize())
                .build();
    }

    /**
     * 이미지 전체 조회 메서드
     * @param placeId 장소 식별자
     * @return List(PlaceImageDto.Response)
     * @author LimJaeMinZ
     * */
    public List<PlaceImageDto.Response> findAllByPlaceImagePath(Long placeId) {

        List<PlaceImage> placeImageList = placeImageRepository.findAllByPlaceId(placeId);

        return placeImageList.stream()
                .map(PlaceImageDto.Response::new)
                .collect(Collectors.toList());
    }

    /**
     * 장소별 이미지 전체 조회 메서드2(객체)
     * @param placeId 장소 식별자
     * @return List(PlaceImage)
     * @author LimJaeMinZ
     */
    public List<PlaceImage> findAllByPlaceImage(Long placeId) {
        return placeImageRepository.findAllByPlaceId(placeId);
    }

    /**
     * 이미지 삭제 메서드
     * @param Id 장소 이미지 식별자
     * @author LimJaeMinZ
     */
    public void delete(Long Id) {
        placeImageRepository.deleteById(Id);
    }
}
