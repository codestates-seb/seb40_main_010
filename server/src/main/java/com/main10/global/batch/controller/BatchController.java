package com.main10.global.batch.controller;

import com.main10.global.batch.service.BatchService;
import com.main10.domain.dto.SingleResponseDto;
import com.main10.domain.place.dto.PlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.main10.domain.member.utils.AuthConstant.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;

    /**
     * BEST MBTI 조회 컨트롤러 메서드
     * @param refreshToken 리프레시 토큰
     * @return ResponseEntity
     * @author mozzi327
     */
    @GetMapping("/mbti")
    public ResponseEntity getMbti(@RequestHeader(value = REFRESH_TOKEN) String refreshToken) {
        List<PlaceDto.Response> result = batchService.getBestMbtiPlaceForMember(refreshToken);
        return ResponseEntity.ok().body(new SingleResponseDto<>(result));
    }

}
