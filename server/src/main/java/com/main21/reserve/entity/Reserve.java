package com.main21.reserve.entity;

import com.main21.place.entity.Place;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

    private int capacity;

    private Date startTime;

    private Date endTime;

    private Long placeId;

    public void addPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    @Builder
    public Reserve(int capacity, Date startTime, Date endTime) {
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
