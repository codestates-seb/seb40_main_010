package com.main21.place.controller;

import com.main21.place.dto.PlaceDto;
import com.main21.place.entity.Place;
import com.main21.place.mapper.PlaceMapper;
import com.main21.place.service.PlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceMapper mapper;

    public PlaceController(PlaceService placeService, PlaceMapper mapper) {
        this.placeService = placeService;
        this.mapper = mapper;
    }

    @PostMapping("place/post")
    public ResponseEntity createPlace(@Valid @RequestBody PlaceDto.Post post) { //추후 유저 아이디 추가
        Place place = placeService.createPlace(post);
        return new ResponseEntity<>(place, HttpStatus.CREATED);
    }

}
