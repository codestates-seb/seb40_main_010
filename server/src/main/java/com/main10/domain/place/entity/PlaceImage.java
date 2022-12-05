package com.main10.domain.place.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicUpdate
public class PlaceImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLACE_IMAGE_ID")
    private Long id;
    private String originFileName; //원본 파일명
    private String fileName;
    private String filePath; //파일 저장 경로
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @Builder
    public PlaceImage(String originFileName, String fileName, String filePath, Long fileSize) {
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public PlaceImage(String filePath, Place place) {
        this.filePath = filePath;
        this.place = place;
    }

    public void setPlace(Place place) {
        this.place = place;
        if(place.getPlaceImages().contains(this)) {
            place.getPlaceImages().add(this);
        }
    }
}
