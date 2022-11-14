package com.main21.security.exception;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import lombok.Getter;

public class AuthException extends BusinessLogicException {

    @Getter
    private final ExceptionCode exceptionCode;

    public AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
    }
}
