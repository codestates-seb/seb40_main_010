package com.main10.domain.place.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
public class PlacePatchDto {
    private Long memberId;
    private Long reserveId;
    private double score;
    private String title;
    private List<String> categoryList;
    private int maxCapacity;
    private String address;
    private String detailInfo;
    private int charge;
    private List<MultipartFile> multipartFiles;
}