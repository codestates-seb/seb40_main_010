package com.main21.reserve.controller;

import com.main21.dto.MultiResponseDto;
import com.main21.reserve.dto.PayApprovalDto;
import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.response.Message;
import com.main21.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.main21.member.utils.AuthConstant.REFRESH_TOKEN;
import static com.main21.reserve.utils.ReserveConstants.*;

@RestController
@RequiredArgsConstructor
public class ReserveController {
    private final ReserveService reserveService;


    /**
     * 예약 프로세스 1 - 예약 등록 컨트롤러 메서드
     *
     * @param placeId  장소 식별자
     * @param post     예약 등록 정보
     * @param refreshToken
     * @return ResonseEntity
     * @author LeeGoh
     */
    @PostMapping("/place/{place-id}/reserve")
    public ResponseEntity postReserve(@PathVariable("place-id") Long placeId,
                                      @RequestBody ReserveDto.Post post,
                                      @RequestHeader(REFRESH_TOKEN) String refreshToken) throws ParseException {
        reserveService.createReserve(post, placeId, refreshToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * 예약 프로세스 2 - 사용자 결제 화면 전송 컨트롤러 메서드
     *
     * @param reserveId 예약 식별자
     * @param refreshToken
     * @param req       요청
     * @return Message
     * @author mozzi327
     */
    @GetMapping("/place/reserve/{reserve-id}/payment")
    public ResponseEntity<Message> orderAction(@PathVariable(name = "reserve-id") Long reserveId,
                                               @RequestHeader(REFRESH_TOKEN) String refreshToken,
                                               HttpServletRequest req) {
        String requestUrl = req.getRequestURL()
                .toString()
                .replace(req.getRequestURI(), "");
        String url = reserveService.getKaKaoPayUrl(reserveId, refreshToken, requestUrl);

        if (url == null) getFailedPayMessage();

        return ResponseEntity
                .ok()
                .body(
                        Message.builder()
                                .data(url)
                                .message(PAY_URI_MSG)
                                .build());
    }


    /**
     * 예약 프로세스 3 - 결제 승인 시 발생되는 컨트롤러 메서드
     *
     * @param pgToken Payment Gateway Token
     * @return PayApprovalDto
     * @author mozzi327
     */
    @GetMapping("/api/order/completed")
    public ResponseEntity<Message> paySuccessAction(@RequestParam("pg_token") String pgToken) {
        PayApprovalDto payInfo = reserveService.getApprovedKaKaoPayInfo(pgToken);

        if (payInfo == null) getFailedPayMessage();

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Message.builder()
                                .data(payInfo)
                                .message(INFO_URI_MSG)
                                .build()
                );
    }


    /**
     * 예약 프로세스 4 - 결제 취소 시 발생되는 컨트롤러 메서드
     *
     * @return ResponseEntity
     * @author mozzi327
     */
    @GetMapping("/api/order/cancel")
    public ResponseEntity<String> payCancelAction() {
        reserveService.setCanceledStatus();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(CANCELED_PAY_MESSAGE);
    }


    /**
     * 예약 프로세스 5 - 결제 실패 시 발생되는 컨트롤러 메서드
     *
     * @return ResponseEntity
     * @author mozzi327
     */
    @GetMapping("/api/order/fail")
    public ResponseEntity<String> payFailedAction() {
        reserveService.setFailedStatus();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(FAILED_PAY_MESSAGE);
    }


    /**
     * URL 요청 시 리턴되는 URL이 없을 때 발생하는 이벤트 메서드
     *
     * @return ResponseEntity
     * @author mozzi327
     */
    public ResponseEntity getFailedPayMessage() {
        return ResponseEntity.badRequest().body(
                Message.builder()
                        .message(FAILED_INFO_MESSAGE + "<br>" + INVALID_PARAMS)
                        .build());
    }


    /* ------------------------------------ 일단 예외 -------------------------------------*/


    /**
     * 예약 수정 컨트롤러 메서드
     *
     * @param reserveId 예약 식별자
     * @param patch     예약 수정 정보
     * @param refreshToken
     * @return ResponseEntity
     * @author Quartz614
     */
    @PatchMapping("place/reserve/{reserve-id}/edit") // 유저 테이블 생성 시 유저 추가
    public ResponseEntity patchReserve(@PathVariable("reserve-id") Long reserveId,
                                       @RequestBody ReserveDto.Patch patch,
                                       @RequestHeader(REFRESH_TOKEN) String refreshToken) {
        reserveService.updateReserve(patch, reserveId, refreshToken);
        return ResponseEntity.ok().build();
    }


    /**
     * 사용자 예약 내역 조회 컨트롤러 메서드
     *
     * @param refreshToken
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/reserve")
    public ResponseEntity getReservations(@RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                                          Pageable pageable) {
        Page<ReserveDto.Response> pageReservations = reserveService.getReservation(refreshToken, pageable);
        List<ReserveDto.Response> reservations = pageReservations.getContent();
        return new ResponseEntity(new MultiResponseDto<>(reservations, pageReservations), HttpStatus.OK);
    }


    /**
     * 예약 내역 삭제 컨트롤러 메서드
     * 예약 내역 상태 변경 및 예약 취소 사유 저장
     *
     * @param reserveId 예약 식별자
     * @param refreshToken
     * @return ResponseEntity
     * @author LeeGoh
     */
    @DeleteMapping("/reserve/{reserve-id}")
    public ResponseEntity deleteReserve(@PathVariable("reserve-id") Long reserveId,
                                        @RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                                        @RequestBody(required = false) ReserveDto.Cancel cancel) {
        reserveService.deleteReserve(cancel, reserveId, refreshToken);
        return new ResponseEntity(HttpStatus.OK);
    }
}