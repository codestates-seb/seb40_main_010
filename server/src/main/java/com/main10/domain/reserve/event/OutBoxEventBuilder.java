package com.main10.domain.reserve.event;

public interface OutBoxEventBuilder <T> {

    OutboxEvent createOutBoxEvent(T domainEvent);
}
