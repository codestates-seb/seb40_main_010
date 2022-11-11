package com.main21.reserve.entity;

import com.main21.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
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
}
