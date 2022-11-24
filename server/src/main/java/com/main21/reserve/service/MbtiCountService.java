package com.main21.reserve.service;

import com.main21.member.entity.Member;
import com.main21.reserve.entity.MbtiCount;
import com.main21.reserve.repository.MbtiCountRepository;
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
     * @param member
     * @param placeId
     */
    public void addMbtiCount(Member member, Long placeId) {
        Optional<MbtiCount> findMBTICount = mbtiCountRepository.findMbtiCountByMbtiAndPlaceId(member.getMbti(), placeId);
        if (!findMBTICount.isPresent()) { // 없을 경우 새로 생성
            MbtiCount saveNewMBTICount = MbtiCount.builder()
                    .mbti(member.getMbti())
                    .placeId(placeId)
                    .totalCount(1)
                    .build();
            mbtiCountRepository.save(saveNewMBTICount);
        } else { // 있을 경우 count를 +1해준다.
            findMBTICount.get().addOneMbti();
            mbtiCountRepository.save(findMBTICount.get());
        }
    }

    /**
     * mbti count 로직(예약 삭 시)
     *
     * @param member
     * @param placeId
     */
    public void reduceMbtiCount(Member member, Long placeId) {
        // mbtiCount -1
        Optional<MbtiCount> findMBTICount = mbtiCountRepository.findMbtiCountByMbtiAndPlaceId(member.getMbti(), placeId);
        findMBTICount.get().reduceOneMbti();
        mbtiCountRepository.save(findMBTICount.get());
    }

}
