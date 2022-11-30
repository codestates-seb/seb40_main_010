package com.main10.domain.place.dto;

import com.main10.domain.member.entity.Member;
import com.main10.domain.place.entity.Place;
import com.main10.domain.reserve.dto.ReserveDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceResponseDto {
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

    public PlaceResponseDto(Place place,
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
