package com.main21.reserve.repository;

import com.main21.reserve.entity.TimeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeStatusRepository extends JpaRepository<TimeStatus, Long> {

    TimeStatus findByHostingTimeIdAndStartTime(Long HostingTimeId, Integer startTime);
}
