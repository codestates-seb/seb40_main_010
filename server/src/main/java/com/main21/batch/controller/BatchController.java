package com.main21.batch.controller;

import com.main21.batch.service.BatchService;
import com.main21.dto.SingleResponseDto;
import com.main21.place.dto.PlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.main21.member.utils.AuthConstant.REFRESH_TOKEN;

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
