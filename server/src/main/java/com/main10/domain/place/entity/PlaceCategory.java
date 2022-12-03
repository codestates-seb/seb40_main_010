package com.main10.domain.place.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PlaceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLACE_CATEGORY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @Column
    private String categoryName;

    public PlaceCategory(Place place, String categoryName, Category category) {
        this.place = place;
        this.categoryName = categoryName;
        this.category = category;
    }

    public void setPlace(Place place) {
        if (this.place != null) {
            this.place.getPlaceCategories().remove(this);
        }
        this.place = place;
        if (place.getPlaceCategories() != this) {
            place.addPlaceCategory(this);
        }
    }
}
