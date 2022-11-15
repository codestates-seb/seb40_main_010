package com.main21.reserve.repository;

import com.main21.reserve.dto.ReserveDto;

import java.util.List;

public interface CustomReserveRepository {
    List<ReserveDto.Response> getReservation(Long memberId);
}
