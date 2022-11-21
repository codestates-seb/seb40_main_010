package com.main21.batch.service;

import com.main21.batch.entity.Best;
import com.main21.batch.repository.BestRepository;
import com.main21.exception.ExceptionCode;
import com.main21.member.entity.Member;
import com.main21.member.repository.MemberRepository;
import com.main21.place.dto.PlaceDto;
import com.main21.place.repository.PlaceRepository;
import com.main21.security.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BatchService {
    private final BestRepository bestRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    /**
     * 회원의 MBTI를 통해 Best MBTI 장소를 가져오는 메서드
     * @param memberId 사용자 식별자
     * @return List(PlaceDto.Response)
     * @author mozzi327
     */
    public List<PlaceDto.Response> getMbtis(Long memberId) {
        String memberMbti;
        if (memberId != null) {
            Member findMember = ifExistMember(memberId);
            memberMbti = findMember.getMbti();
        } else memberMbti = "NONE";

        List<Best> bestList = bestRepository.findAllByMbtiOrderByTotalCountDesc(memberMbti);

        return bestList.stream()
                .map(best -> placeRepository.getPlace(best.getPlaceId()))
                .collect(Collectors.toList());
    }


    /**
     * 사용자 식별자를 통해 사용자 정보가 존재하는지 확인하는 메서드
     *
     * @param memberId 사용자 식별자
     * @return Member
     * @author mozzi327
     */
    public Member ifExistMember(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new AuthException(ExceptionCode.MEMBER_NOT_FOUND));
    }

}
