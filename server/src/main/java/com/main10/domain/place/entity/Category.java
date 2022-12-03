package com.main10.domain.place.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<PlaceCategory> placeCategories = new ArrayList<>();
}
