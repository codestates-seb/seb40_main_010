package com.main21.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlacePostDto {
    private Long memberId;
    private Long reserveId;
    @NotBlank
    private double score;
    @NotBlank
    private String title;
    private List<String> categoryList;
    @NotBlank
    private int maxCapacity;
    @NotBlank
    private String address;
    @NotBlank
    private String detailInfo;
    @NotBlank
    private int charge;
    @NotBlank
    private Integer endTime;
}
