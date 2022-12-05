package com.main10.domain.reserve.repository;

import com.main10.domain.reserve.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long>, CustomReserveRepository {
    List<Reserve> findAllByPlaceIdAndStatus(Long placeId, Reserve.ReserveStatus reserveStatus);
    Reserve findByPlaceId(Long placeId);
}
