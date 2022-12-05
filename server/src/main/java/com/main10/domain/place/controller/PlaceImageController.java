package com.main10.domain.place.controller;

import com.main10.domain.place.service.PlaceImageService;
import com.main10.domain.place.dto.PlaceImageDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class PlaceImageController {

    private final PlaceImageService placeImageService;

    /**
     * 썸네일용 이미지 조회 메서드
     * @param id 장소 이미지 식별자
     * @author LimJaeMinZ
     */
    @CrossOrigin
    @GetMapping(value = "/thumbnail/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) throws IOException {

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String path;

        // 전달되어 온 이미지가 기본 썸네일이 아닌 경우
        if(id != 0) {
            PlaceImageDto placeImageDto = placeImageService.findByFieldId(id);
            path = placeImageDto.getFilePath();
        }
        // 전달되어 온 이미지가 기본 썸네일인 경우
        else {
            path = "images" + File.separator + "thumbnail" + File.separator + "thumbnail.png";
        }

        //FileInputStream 객체를 생성하여 이미지가 저장된 경로를 byte[] 형태의 값으로 encoding
        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return ResponseEntity.ok(imageByteArray);
    }

    /**
     * 이미지 개별 조회 메서드
     * @param id 장소 이미지 식별자
     * @author LimJaeMinZ
     */
    @CrossOrigin
    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws IOException {
        PlaceImageDto placeImageDto = placeImageService.findByFieldId(id);
        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String path = placeImageDto.getFilePath();

        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return ResponseEntity.ok(imageByteArray);
    }
}
