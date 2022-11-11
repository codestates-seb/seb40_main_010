package com.main21.place.entity;

import lombok.Builder;
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

    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<PlaceCategory> placeCategories = new ArrayList<>();
}
