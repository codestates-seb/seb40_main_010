package com.main21.security.exception;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import lombok.Getter;

/**
 * BusinessException의 역할 세분화를 위한 Exception 클래스
 * @author mozzi327
 */
public class AuthException extends BusinessLogicException {

    @Getter
    private final ExceptionCode exceptionCode;

    public AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
    }
}
