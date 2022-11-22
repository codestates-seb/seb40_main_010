package com.main21.place.dto;

import com.main21.place.entity.PlaceImage;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@Getter
public class PlacePostDto {
    private Long memberId;
    private Long reserveId;
    @NotBlank
    private double score;
    private int view;
    @NotBlank
    private String title;
    private List<String> categoryList;
    @NotBlank
    private int maxCapacity;
    @NotBlank
    private Integer maxSpace;
    @NotBlank
    private String address;
    @NotBlank
    private String detailInfo;
    @NotBlank
    private int charge;
    @NotBlank
    private Integer endTime;
}
