package com.main10.domain.reserve.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.main10.domain.reserve.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveEventBuilder implements OutBoxEventBuilder<ReserveCreated> {

    private static final String EVENT_ACTION = "예약 결제";

    @Override
    public OutboxEvent createOutBoxEvent(ReserveCreated domainEvent) {
        JsonNode jsonNode = ObjectMapperUtil
                .getMapper().
                convertValue(domainEvent, JsonNode.class);

        return new OutboxEvent.OutboxEventBuilder()
                .aggregateId((long) domainEvent.getItemNumber())
                .aggregateType(ReserveCreated.class.getSimpleName())
                .eventType(domainEvent.getClass().getSimpleName())
                .payload(jsonNode.toString())
                .eventAction(EVENT_ACTION)
                .build();
    }
}
