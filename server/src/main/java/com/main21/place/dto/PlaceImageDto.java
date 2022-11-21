package com.main21.place.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class PlaceImageDto {
    @NotBlank
    private String originFileName;
    @NotBlank
    private String fileName;
    @NotBlank
    private String filePath;
    private Long fileSize;
}
