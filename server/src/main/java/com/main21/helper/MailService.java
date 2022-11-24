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
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
@AllArgsConstructor
public class MailService {

    private JavaMailSender mailSender;
    private ReserveDbService reserveDbService;
    private PlaceDbService placeDbService;
    private MemberDbService memberDbService;
    private RedisUtils redisUtils;
    private static final String FROM_ADDRESS = "YOUR_EMAIL_ADDRESS";

    @Async
    public void mailSend(String refreshToken, Long reserveId) {

        Long memberId = redisUtils.getId(refreshToken);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);
        Place findPlace = placeDbService.ifExistsReturnPlace(findReserve.getPlaceId());
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

        if (!memberId.equals(findReserve.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.MISMATCH_ACCESS_TOKEN);
        }

        String mailTitle = findMember.getNickname() + "님 예약이 등록되었습니다.";
        String text = findMember.getNickname() + "님의 예약이 확정되었습니다. 예약 내용은 아래와 같습니다.\n" +
                "\nPlace: " + findPlace.getTitle() +
                "\nAddress: " + findPlace.getAddress() +
                "\nCheckIn: " + findReserve.getStartTime().getYear() + "년 " +
                findReserve.getStartTime().getMonthValue() + "월 " +
                findReserve.getStartTime().getDayOfMonth() + "일 "+
                findReserve.getStartTime().getHour() + "시" +
                "\nCheckOut: " + findReserve.getEndTime().getYear() + "년 " +
                findReserve.getEndTime().getMonthValue() + "월 " +
                findReserve.getEndTime().getDayOfMonth() + "일 "+
                findReserve.getEndTime().getHour() + "시" +
                "\nCapacity: " + findReserve.getCapacity() + "명" +
                "\nTotalCharge: " + findReserve.getTotalCharge() + "원" +
                "\n\n 좋은 시간 되시길 바랍니다:)";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(findMember.getEmail());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailTitle);
        message.setText(text);
        mailSender.send(message);

    }
}
