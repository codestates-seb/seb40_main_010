package com.main10.domain.place.repository;

import com.main10.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long>, CustomPlaceRepository {
}
