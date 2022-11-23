package com.main21.reserve.repository;

import com.main21.reserve.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long>, CustomReserveRepository {
    List<Reserve> findAllByPlaceId(Long placeId);
    Reserve findByPlaceId(Long placeId);
}
