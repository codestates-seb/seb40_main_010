package com.main21.batch.repository;

import com.main21.batch.entity.MbtiCount;

import java.util.List;

public interface CustomBestRepository {
    List<MbtiCount> calcBest(String mbti);
}
