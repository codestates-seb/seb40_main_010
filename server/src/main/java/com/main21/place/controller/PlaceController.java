package com.main21.place.controller;

import com.main21.place.dto.PlacePostDto;

import com.main21.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

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

}
