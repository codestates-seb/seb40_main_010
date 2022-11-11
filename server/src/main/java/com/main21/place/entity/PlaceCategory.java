package com.main21.place.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
public class PlaceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLACE_CATEGORY_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    public void addPlace(Place place) {
        if (this.place != null) {
            this.place.getPlaceCategories().remove(this);
        }
        this.place = place;
        if (place.getPlaceCategories() != this) {
            place.addPlaceCategory(this);
        }
    }
}
