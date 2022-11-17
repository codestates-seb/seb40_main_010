package com.main21.payment.outbox.event;

public interface OutBoxEventBuilder <T> {

    OutboxEvent createOutBoxEvent(T domainEvent);
}
