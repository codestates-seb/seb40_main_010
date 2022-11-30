package com.main10.global.batch.repository;

import com.main10.global.batch.entity.MbtiCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MbtiCountRepository extends JpaRepository<MbtiCount, Long> {
    Optional<MbtiCount> findMbtiCountByMbtiAndPlaceId(String mbti, Long placeId);
}
