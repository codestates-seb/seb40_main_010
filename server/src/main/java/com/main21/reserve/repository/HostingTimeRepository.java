package com.main21.reserve.repository;

import com.main21.reserve.entity.HostingTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HostingTimeRepository extends JpaRepository<HostingTime, Long> {

    Optional<HostingTime> findByPlaceIdAndReserveDate(Long placeId, String reserveDate);

    void deleteAllByPlaceId(Long placeId);

    List<HostingTime> findByPlaceIdAndReserveDateGreaterThanEqual(Long placeId, String reserveDate);

}
