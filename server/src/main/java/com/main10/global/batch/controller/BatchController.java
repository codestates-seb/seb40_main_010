package com.main10.global.batch.controller;

import com.main10.global.batch.service.BatchService;
import com.main10.domain.dto.SingleResponseDto;
import com.main10.domain.place.dto.PlaceDto;
import com.main10.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;

    /**
     * BEST MBTI 조회 컨트롤러 메서드
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author mozzi327
     */
    @GetMapping("/mbti")
    public ResponseEntity<SingleResponseDto<?>> getMbti(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId;
        if (token == null) memberId = null;
        else memberId = token.getId();
        List<PlaceDto.Response> result = batchService.getBestMbtiPlaceForMember(memberId);
        return ResponseEntity.ok().body(new SingleResponseDto<>(result));
    }

}
