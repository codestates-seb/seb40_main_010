package com.main21.place.mapper;

import com.main21.place.dto.PlaceDto;
import com.main21.place.entity.Place;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    Place placePostToPlace(PlaceDto.Post post);
}
