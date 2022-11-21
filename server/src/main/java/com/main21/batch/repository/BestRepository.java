package com.main21.batch.repository;

import com.main21.batch.entity.Best;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestRepository extends JpaRepository<Best, Long>, CustomBestRepository {
}
