package com.main21.place.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlaceCategoryDto {

    @Getter
    @NoArgsConstructor
    public static class Search {
        private Long categoryId;
        private String categoryName;
    }
}
