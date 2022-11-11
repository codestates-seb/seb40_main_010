package com.main21.exception;

import lombok.Getter;

public enum ExceptionCode {
    PLACE_NOT_FOUND(504, "Place not found");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
