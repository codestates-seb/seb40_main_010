package com.main10.global.batch.service;

import com.main10.domain.member.entity.Member;
import com.main10.global.batch.entity.MbtiCount;
import com.main10.global.batch.repository.MbtiCountRepository;
import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MbtiCountService {
    private final MbtiCountRepository mbtiCountRepository;

    /**
     * mbti count 로직(결제 성공 시)
     *
     * @param member 조회 사용자 정보
     * @param placeId 장소 식별자
     * @author LimJaeMinZ
     */
    public void addMbtiCount(Member member, Long placeId) {
        Optional<MbtiCount> findMBTICount = mbtiCountRepository.findMbtiCountByMbtiAndPlaceId(member.getMbti(), placeId);
        if (findMBTICount.isPresent()) { // 있을 경우 count를 +1해준다.
            findMBTICount.get().addOneMbti();
            mbtiCountRepository.save(findMBTICount.get());
        } else { // 없을 경우 새로 생성
            MbtiCount saveNewMBTICount = MbtiCount.builder()
                    .mbti(member.getMbti())
                    .placeId(placeId)
                    .totalCount(1)
                    .build();
            mbtiCountRepository.save(saveNewMBTICount);
        }
    }

    /**
     * mbti count 로직(예약 삭제 시)
     *
     * @param member 조회된 사용자 정보
     * @param placeId 장소 식별자
     * @author LimJaeMinZ
     */
    public void reduceMbtiCount(Member member, Long placeId) {
        // mbtiCount -1
        MbtiCount findMBTICount = mbtiCountRepository
                .findMbtiCountByMbtiAndPlaceId(member.getMbti(), placeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MBTI_COUNT_NOT_FOUND));
        findMBTICount.reduceOneMbti();
        mbtiCountRepository.save(findMBTICount);
    }

}
