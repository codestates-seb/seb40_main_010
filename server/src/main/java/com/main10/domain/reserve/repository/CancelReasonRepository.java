package com.main10.domain.reserve.repository;

import com.main10.domain.reserve.entity.CancelReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelReasonRepository extends JpaRepository<CancelReason, Long> {
}
