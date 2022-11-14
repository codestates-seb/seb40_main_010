package com.main21.reserve.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
   
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Long placeId;

    public void addPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    @Builder
    public Reserve(int capacity, Date startTime, Date endTime, Long placeId) {
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.placeId = placeId;
    }
    public void editReserve(int capacity, Date startTime, Date endTime){
        this.capacity = capacity;
        this. startTime = startTime;
        this. endTime = endTime;
    }
}
