package com.main21.reserve.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.entity.Place;
import com.main21.reserve.entity.HostingTime;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.entity.TimeStatus;
import com.main21.reserve.repository.HostingTimeRepository;
import com.main21.reserve.repository.TimeStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostingTimeService {

    private final ReserveDbService reserveDbService;
    private final HostingTimeRepository hostingTimeRepository;
    private final TimeStatusRepository timeStatusRepository;

    private Integer reserveStartHH;
    private Integer reserveEndHH;

    /**
     * HostingTime, TimeStatus
     * 예약 등록 시 maxSpace, spaceCount, isFull 메서드
     *
     * @param reserve
     * @param place
     */
    public void addSpaceCount(Reserve reserve, Place place) {

        SimpleDateFormat fDate = new SimpleDateFormat("yyyy-MM-dd");
        String reserveDay = fDate.format(reserve.getStartTime());

        reserveStartHH = Integer.parseInt(new SimpleDateFormat("HH").format(reserve.getStartTime()));
        reserveEndHH = Integer.parseInt(new SimpleDateFormat("HH").format(reserve.getEndTime()));

        Optional<HostingTime> findHostingTime = hostingTimeRepository.findByPlaceIdAndReserveDate(place.getId(), reserveDay);

        if (!findHostingTime.isPresent()) {
            HostingTime hostingTime = HostingTime.builder()
                    .reserveDate(reserveDay)
                    .placeId(reserve.getPlaceId())
                    .build();
            hostingTimeRepository.save(hostingTime);

            for (int i = reserveStartHH; i < reserveEndHH; i++) {
                TimeStatus timeStatus = new TimeStatus(hostingTime, i, i + 1);
                timeStatus.addSpaceCount();
                timeStatusRepository.save(timeStatus);
            }

        } else {
            for (int i = reserveStartHH; i < reserveEndHH; i++) {
                TimeStatus findTimeStatus = timeStatusRepository.findByHostingTimeIdAndStartTime(findHostingTime.get().getId(), i);
                if (findTimeStatus == null) {
                    TimeStatus timeStatus = new TimeStatus(findHostingTime.get(), i, i + 1);
                    timeStatus.addSpaceCount();
                    timeStatusRepository.save(timeStatus);
                } else {
                    if (!findTimeStatus.isFull()) {
                        findTimeStatus.addSpaceCount();
                        if (findTimeStatus.getSpaceCount().equals(place.getMaxSpace())) {
                            findTimeStatus.setIsFull();
                        }
                        timeStatusRepository.save(findTimeStatus);
                    } else {
                        throw new IllegalAccessError("Full space");
                    }
                }
            }
        }

        if (reserve.getCapacity() > place.getMaxCapacity()) {
            throw new BusinessLogicException(ExceptionCode.RESERVATION_MAX_CAPACITY_OVER);
        } else reserveDbService.saveReserve(reserve);
    }

    /**
     * HostingTime, TimeStatus
     * 예약 삭제(취소) 시 spaceCount -1 메서드
     *
     * @param reserve
     * @param place
     * @author LeeGoh
     */
    public void reduceSpaceCount(Reserve reserve, Place place) {
        // spaceCount -1
        SimpleDateFormat fDate = new SimpleDateFormat("yyyy-MM-dd");
        String reserveDay = fDate.format(reserve.getStartTime());

        reserveStartHH = Integer.parseInt(new SimpleDateFormat("HH").format(reserve.getStartTime()));
        reserveEndHH = Integer.parseInt(new SimpleDateFormat("HH").format(reserve.getEndTime()));

        Optional<HostingTime> findHostingTime = hostingTimeRepository.findByPlaceIdAndReserveDate(place.getId(), reserveDay);

        for (int i = reserveStartHH; i < reserveEndHH; i++) {
            // 시간대마다 spaceCount -1
            TimeStatus findTimeStatus = timeStatusRepository.findByHostingTimeIdAndStartTime(findHostingTime.get().getId(), i);
            findTimeStatus.reduceSpaceCount();

            // spaceCount의 값과 maxSpace의 값이 같지 않으면 isFull = false
            if (!findTimeStatus.getSpaceCount().equals(place.getMaxSpace())) {
                findTimeStatus.setIsNotFull();
            }

            timeStatusRepository.save(findTimeStatus);
        }
    }


}
