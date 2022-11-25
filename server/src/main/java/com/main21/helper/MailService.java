package com.main21.helper;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.member.entity.Member;
import com.main21.member.service.MemberDbService;
import com.main21.place.entity.Place;
import com.main21.place.service.PlaceDbService;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.service.ReserveDbService;
import com.main21.security.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    private ReserveDbService reserveDbService;
    private PlaceDbService placeDbService;
    private MemberDbService memberDbService;
    private RedisUtils redisUtils;
    private static final String FROM_ADDRESS = "YOUR_EMAIL_ADDRESS";

    public MailService(ReserveDbService reserveDbService,
                       PlaceDbService placeDbService,
                       MemberDbService memberDbService,
                       RedisUtils redisUtils) {
        this.reserveDbService = reserveDbService;
        this.placeDbService = placeDbService;
        this.memberDbService = memberDbService;
        this.redisUtils = redisUtils;
    }

    /**
     * 예약 확정(결제 완료) 내역 메일 전송 메서드
     *
     * @param refreshToken 리프래시 토큰
     * @param reserveId 예약 식별자
     * @author LeeGoh
     */
    @Async
    public void mailSend(String refreshToken, Long reserveId) {

        // 예약, 공간, 회원 찾아오기
        Long memberId = redisUtils.getId(refreshToken);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);
        Place findPlace = placeDbService.ifExistsReturnPlace(findReserve.getPlaceId());
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

        // refreshToken 값과 예약의 memberId가 다르면 에러
        if (!memberId.equals(findReserve.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.MISMATCH_ACCESS_TOKEN);
        }

        // 메일 제목
        String mailTitle = String.format("[대여가대여] %s님 예약이 등록되었습니다.", findMember.getNickname());

        // 메일 내용
        String text = String.format("%s님의 예약이 확정되었습니다. 예약 내용은 아래와 같습니다.\n" +
                        "\nPlace: %s " +
                        "\nAddress: %s " +
                        "\nCheck-In: %tY년 %<tm월 %<td일 %<tH시" +
                        "\nCheck-Out: %tY년 %<tm월 %<td일 %<tH시" +
                        "\nCapacity: %d명" +
                        "\nTotalCharge: %d원" +
                        "\n\n 좋은 시간 되시길 바랍니다:)",
                        findMember.getNickname(),
                        findPlace.getTitle(), findPlace.getAddress(),
                        findReserve.getStartTime(),
                        findReserve.getEndTime(),
                        findReserve.getCapacity(), findReserve.getTotalCharge());

        // 회원의 email로 메일 send
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(findMember.getEmail());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailTitle);
        message.setText(text);
        mailSender.send(message);

    }
}
