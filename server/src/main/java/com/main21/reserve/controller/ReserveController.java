package com.main21.reserve.controller;

import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReserveController {
    private final ReserveService reserveService;

    @PostMapping("/place/{place-id}/reserve") //유저 테이블 생성 시 유저 추가
    public ResponseEntity postReserve(@PathVariable("place-id") Long placeId,
                                      @RequestBody ReserveDto.Post post) {

        Reserve reserve = reserveService.createReserve(post, placeId);

        return new ResponseEntity<>(reserve, HttpStatus.CREATED);
    }
    @PatchMapping("place/reserve/{reserve-id}/edit") // 유저 테이블 생성 시 유저 추가
    public ResponseEntity patchReserve(@PathVariable("reserve-id") Long reserveId,
                                       @RequestBody ReserveDto.Patch patch) {
        Reserve reserve = reserveService.updateReserve(patch, reserveId);
        return new ResponseEntity<>(reserve, HttpStatus.OK);
    }
}
