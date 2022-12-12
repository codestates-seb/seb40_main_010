package com.main10.global.batch.process;

import com.main10.global.batch.entity.Best;
import com.main10.global.batch.repository.BestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class RankingProcess {

    private final BestRepository bestRepository;

    private final List<String> mbtis = List.of(
            "ENFJ", "ENTJ", "ENFP", "ENTP", "ESFP",
            "ESFJ", "ESTP", "ESTJ", "INFP", "INFJ", "INTP",
            "ISTP", "ISFP", "ISFJ", "ISTJ", "INTJ", "NONE"
    );

    @Scheduled(cron = "0 59 23 * * *")
    public void calcBestMBTI() {
        bestRepository.deleteAll();
        mbtis.forEach(mbti -> bestRepository
                .calcBest(mbti).forEach(mbtiCount -> bestRepository.save(Best.builder()
                        .mbti(mbtiCount.getMbti())
                        .placeId(mbtiCount.getPlaceId())
                        .totalCount(mbtiCount.getTotalCount())
                        .build())));
        log.info("계산 완료 :::::::::> Time : " + new Date());
    }
}