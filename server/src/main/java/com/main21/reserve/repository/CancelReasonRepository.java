package com.main21.reserve.repository;

import com.main21.reserve.entity.CancelReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelReasonRepository extends JpaRepository<CancelReason, Long> {
}
