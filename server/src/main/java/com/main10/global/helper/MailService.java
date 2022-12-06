package com.main10.global.helper;

import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.service.MemberDbService;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.service.PlaceDbService;
import com.main10.domain.reserve.entity.Reserve;
import com.main10.domain.reserve.service.ReserveDbService;
import com.main10.global.security.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    private final ReserveDbService reserveDbService;
    private final PlaceDbService placeDbService;
    private final MemberDbService memberDbService;
    private static final String FROM_ADDRESS = "YOUR_EMAIL_ADDRESS";

    public MailService(ReserveDbService reserveDbService,
                       PlaceDbService placeDbService,
                       MemberDbService memberDbService) {
        this.reserveDbService = reserveDbService;
        this.placeDbService = placeDbService;
        this.memberDbService = memberDbService;
    }

    /**
     * 예약 확정(결제 완료) 내역 메일 전송 메서드
     *
     * @param memberId 사용자 식별자
     * @param reserveId 예약 식별자
     * @author LeeGoh
     */
    @Async
    public void mailSend(Long memberId, Long reserveId) {

        // 예약, 공간, 회원 찾아오기
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
