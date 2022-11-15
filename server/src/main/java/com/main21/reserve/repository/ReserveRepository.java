package com.main21.reserve.repository;

import com.main21.reserve.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long>, CustomReserveRepository {
}
