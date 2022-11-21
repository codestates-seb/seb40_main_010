package com.main21.reserve.repository;

import com.main21.reserve.entity.MbtiCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MbtiCountRepository extends JpaRepository<MbtiCount, Long> {
    Optional<MbtiCount> findMbtiCountByMbtiAndPlaceId(String mbti, Long placeId);
}
