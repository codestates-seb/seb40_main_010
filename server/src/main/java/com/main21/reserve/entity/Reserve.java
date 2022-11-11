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

    private Date checkIn;

    private Date checkOut;

    @ManyToOne
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @Builder
    public Reserve(int capacity, Date checkIn, Date checkOut) {
        this.capacity = capacity;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }


    // 편의 메서드
    public void addPlace(Place place) {
        if (this.place != null) {
            this.place.getReserves().remove(this);
        }
        this.place = place;
        if (place.getReserves() != this) {
            place.addReserve(this);
        }
    }
}
