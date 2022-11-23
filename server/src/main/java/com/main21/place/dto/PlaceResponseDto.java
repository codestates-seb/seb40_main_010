package com.main21.place.dto;

import com.main21.member.entity.Member;
import com.main21.place.entity.Place;
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


    public PlaceResponseDto(Place place, List<String> filePath, List<String> category, Member member, boolean isBookmark) {
        this.placeId = place.getId();
        this.title = place.getTitle();
        this.category = category;
        this.maxCapacity = place.getMaxCapacity();
        this.detailInfo = place.getDetailInfo();
        this.charge = place.getCharge();
        this.score = place.getScore();
        this.filePath = filePath;
        this.endTime = place.getEndTIme();
        this.address = place.getAddress();
        this.nickname = member.getNickname();
        this.phoneNumber = member.getPhoneNumber();
        this.isBookmark = isBookmark;
    }
}
