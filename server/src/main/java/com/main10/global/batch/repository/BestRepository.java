package com.main10.global.batch.repository;

import com.main10.global.batch.entity.Best;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BestRepository extends JpaRepository<Best, Long>, CustomBestRepository {

    List<Best> findAllByMbtiOrderByTotalCountDesc(String mbti);
}
