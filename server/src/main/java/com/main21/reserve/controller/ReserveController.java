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
import java.util.List;

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
     * @param memberId 사용자 식별자
     * @return ResonseEntity
     * @author LeeGoh
     */
    @PostMapping("/place/{place-id}/reserve")
    public ResponseEntity postReserve(@PathVariable("place-id") Long placeId,
                                      @RequestBody ReserveDto.Post post,
                                      @CookieValue(name = "memberId") Long memberId) {
        reserveService.createReserve(post, placeId, memberId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * 예약 프로세스 2 - 사용자 결제 화면 전송 컨트롤러 메서드
     *
     * @param reserveId 예약 식별자
     * @param memberId  사용자 식별자
     * @param req       요청
     * @return Message
     * @author mozzi327
     */
    @GetMapping("/place/reserve/{reserve-id}/payment")
    public ResponseEntity<Message> orderAction(@PathVariable(name = "reserve-id") Long reserveId,
                                               @CookieValue(name = "memberId") Long memberId,
                                               HttpServletRequest req) {
        String requestUrl = req.getRequestURL()
                .toString()
                .replace(req.getRequestURI(), "");
        String url = reserveService.getKaKaoPayUrl(reserveId, memberId, requestUrl);

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
     * @param memberId  사용자 식별자
     * @return ResponseEntity
     * @author Quartz614
     */
    @PatchMapping("place/reserve/{reserve-id}/edit") // 유저 테이블 생성 시 유저 추가
    public ResponseEntity patchReserve(@PathVariable("reserve-id") Long reserveId,
                                       @RequestBody ReserveDto.Patch patch,
                                       @CookieValue(name = "memberId") Long memberId) {
        reserveService.updateReserve(patch, reserveId, memberId);
        return ResponseEntity.ok().build();
    }


    /**
     * 사용자 예약 내역 조회 컨트롤러 메서드
     *
     * @param memberId 사용자 식별자
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/reserve")
    public ResponseEntity getReservations(@CookieValue(name = "memberId") Long memberId,
                                          Pageable pageable) {
        Page<ReserveDto.Response> pageReservations = reserveService.getReservation(memberId, pageable);
        List<ReserveDto.Response> reservations = pageReservations.getContent();
        return new ResponseEntity(new MultiResponseDto<>(reservations, pageReservations), HttpStatus.OK);
    }


    /**
     * 예약 내역 삭제 컨트롤러 메서드
     *
     * @param reserveId 예약 식별자
     * @return ResponseEntity
     * @author LeeGoh
     */
    @DeleteMapping("/reserve/{reserve-id}")
    public ResponseEntity deleteReserve(@PathVariable("reserve-id") Long reserveId) {
        reserveService.deleteReserve(reserveId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}