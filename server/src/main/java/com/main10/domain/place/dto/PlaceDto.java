package com.main10.domain.place.dto;

import com.main10.domain.member.entity.Member;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.entity.PlaceImage;
import com.main10.domain.reserve.dto.ReserveDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

public class PlaceDto {

    @Getter
    @NoArgsConstructor
    public static class Create {
        private Long memberId;

        private Long reserveId;

        private double score;

        @NotBlank(message = "장소명은 공백이 아니어야 합니다.")
        @Length(max = 20, message = "글자 수는 최대 20자 이하여야 합니다.")
        private String title;

        private List<String> categoryList;

        @NotNull(message = "최대 인원 수를 작성해 주세요")
        @Positive(message = "최대 인원수는 1명 이상이어야 합니다.")
        private int maxCapacity;

        @NotBlank(message = "주소는 공백이 아니어야 합니다.")
        @Length(max = 255, message = "주소는 255자 이하여야 합니다.")
        private String address;

        @NotBlank(message = "상세 설명은 공백이 아니어야 합니다.")
        @Length(max = 10000, message = "상세 설명은 10000자 이하여야 합니다.")
        private String detailInfo;

        @NotNull(message = "가격은 공백이 아니어야 합니다.")
        @Positive(message = "가격은 1원 이상이어야 합니다.")
        private int charge;

        private Integer endTime;

        @Builder
        public Create(Long memberId, Long reserveId, double score, String title, List<String> categoryList,
                      int maxCapacity, String address, String detailInfo, int charge, Integer endTime) {
            this.memberId = memberId;
            this.reserveId = reserveId;
            this.score = score;
            this.title = title;
            this.categoryList = categoryList;
            this.maxCapacity = maxCapacity;
            this.address = address;
            this.detailInfo = detailInfo;
            this.charge = charge;
            this.endTime = endTime;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Update {
        private Long memberId;
        private Long reserveId;

        @NotNull
        private double score;

        @NotBlank(message = "장소명은 공백이 아니어야 합니다.")
        @Length(max = 20, message = "글자 수는 최대 20자 이하여야 합니다.")
        private String title;
        private List<String> categoryList;

        @NotNull
        @Positive(message = "최대 인원수는 1명 이상이어야 합니다.")
        private int maxCapacity;

        @NotBlank(message = "주소는 공백이 아니어야 합니다.")
        @Length(max = 255, message = "주소는 255자 이하여야 합니다.")
        private String address;

        @NotBlank(message = "상세 설명은 공백이 아니어야 합니다.")
        @Length(max = 10000, message = "상세 설명은 10000자 이하여야 합니다.")
        private String detailInfo;

        @Positive(message = "가격은 1원 이상이어야 합니다.")
        private int charge;
        private Integer endTime;
        private List<MultipartFile> multipartFiles;

        @Builder
        public Update(Long memberId, Long reserveId, double score, String title, List<String> categoryList,
                      int maxCapacity, String address, String detailInfo, int charge, int endTime, List<MultipartFile> multipartFiles) {
            this.memberId = memberId;
            this.reserveId = reserveId;
            this.score = score;
            this.title = title;
            this.categoryList = categoryList;
            this.maxCapacity = maxCapacity;
            this.address = address;
            this.detailInfo = detailInfo;
            this.charge = charge;
            this.endTime = endTime;
            this.multipartFiles = multipartFiles;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DetailResponse {
        private Long placeId;
        private String title;
        private List<String> category;
        private int maxCapacity;
        private String detailInfo;
        private int charge;
        private double score;
        private List<String> filePath;
        private Integer endTime;
        private String address;
        private String nickname;
        private String phoneNumber;
        private boolean isBookmark;
        private List<ReserveDto.Detail> reserves;

        @Builder
        public DetailResponse(Place place,
                              List<String> filePath,
                              List<String> category,
                              Member member,
                              boolean isBookmark,
                              List<ReserveDto.Detail> reserves) {
            this.placeId = place.getId();
            this.title = place.getTitle();
            this.category = category;
            this.maxCapacity = place.getMaxCapacity();
            this.detailInfo = place.getDetailInfo();
            this.charge = place.getCharge();
            this.score = place.getScore();
            this.filePath = filePath;
            this.endTime = place.getEndTime();
            this.address = place.getAddress();
            this.nickname = member.getNickname();
            this.phoneNumber = member.getPhoneNumber();
            this.isBookmark = isBookmark;
            this.reserves = reserves;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchDetail {

        private int startCharge;

        private int endCharge;

        private int capacity;
    }

    @Getter
    public static class Response{
        private final Long placeId;
        private final String title;
        private final int charge;
        private final String image;
        private final Double score;
        private final String address;
        private final Integer endTime;

        @Builder
        @QueryProjection
        public Response(Place place) {
            this.placeId = place.getId();
            this.title = place.getTitle();
            this.score = place.getScore();
            this.charge = place.getCharge();
            this.address = place.getAddress();
            this.endTime = place.getEndTime();
            this.image = place.getPlaceImages().get(0).getFilePath();
        }
    }

    @Getter
    public static class ResponseTest{
        private final Long placeId;
        private final String title;
        private final int charge;
        private final String image;
        private final Double score;
        private final String address;
        private final Integer endTime;

        @Builder
        public ResponseTest(Place place, PlaceImage placeImage) {
            this.placeId = place.getId();
            this.title = place.getTitle();
            this.score = place.getScore();
            this.charge = place.getCharge();
            this.address = place.getAddress();
            this.endTime = place.getEndTime();
            this.image = placeImage.getFilePath();
        }
    }
}
