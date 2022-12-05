package com.main10.global.batch.repository;

import com.main10.global.batch.entity.MbtiCount;

import java.util.List;

public interface CustomBestRepository {
    List<MbtiCount> calcBest(String mbti);
}
