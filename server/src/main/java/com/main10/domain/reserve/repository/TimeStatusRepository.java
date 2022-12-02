package com.main10.domain.reserve.repository;

import com.main10.domain.reserve.entity.TimeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeStatusRepository extends JpaRepository<TimeStatus, Long> {

    TimeStatus findByHostingTimeIdAndStartTime(Long HostingTimeId, Integer startTime);
}
