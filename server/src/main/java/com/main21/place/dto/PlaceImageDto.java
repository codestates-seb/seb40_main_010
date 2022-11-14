package com.main21.place.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlaceImageDto {
    private String fileName;
    private String filePath;
    private Long fileSize;
}
