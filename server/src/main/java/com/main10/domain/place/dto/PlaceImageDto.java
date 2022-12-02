package com.main10.domain.place.dto;

import com.main10.domain.place.entity.PlaceImage;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    @NoArgsConstructor
    public static class Response {
        private String filePath;
        @Builder
        public Response(PlaceImage placeImage) {
            this.filePath = placeImage.getFilePath();
        }
    }
}
