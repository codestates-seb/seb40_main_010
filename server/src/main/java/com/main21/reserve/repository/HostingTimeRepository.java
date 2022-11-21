package com.main21.reserve.repository;

import com.main21.reserve.entity.HostingTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostingTimeRepository extends JpaRepository<HostingTime, Long> {
    Optional<HostingTime> findByReserveDate(String reserveDate);
}
