package com.main10.domain.reserve.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OutboxEvent {

    private Long aggregateId;
    private String aggregateType;
    private String eventType;
    private String payload;
    private String eventAction;
    private List<ReserveCreated> cartList;
}
