package com.main21.batch.controller;

import com.main21.batch.service.BatchService;
import com.main21.dto.SingleResponseDto;
import com.main21.place.dto.PlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;

    /**
     * BEST MBTI 조회 컨트롤러 메서드
     * @param memberId 사용자 식별자
     * @return ResponseEntity
     * @author mozzi327
     */
    @GetMapping("/mbti")
    public ResponseEntity getMbti(@CookieValue(name = "memberId") Long memberId) {
        List<PlaceDto.Response> result = batchService.getMbtis(memberId);
        return ResponseEntity.ok().body(new SingleResponseDto<>(result));
    }

}
