package com.main10.domain.reserve.repository;

import com.main10.domain.reserve.dto.ReserveDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomReserveRepository {
    Page<ReserveDto.Response> getReservation(Long memberId, Pageable pageable);

    List<ReserveDto.Detail> getDetailReservationTime(Long placeId);
}
