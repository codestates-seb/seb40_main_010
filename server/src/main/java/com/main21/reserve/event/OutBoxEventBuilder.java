package com.main21.reserve.event;

public interface OutBoxEventBuilder <T> {

    OutboxEvent createOutBoxEvent(T domainEvent);
}
