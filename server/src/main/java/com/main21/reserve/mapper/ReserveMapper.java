package com.main21.reserve.mapper;

import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.entity.Reserve;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReserveMapper {
    Reserve reservePostDtoToReserveDto(ReserveDto.Post post);
}
