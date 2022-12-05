package com.main10.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        return Response.of(e.getBindingResult());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleConstraintViolationException(
            ConstraintViolationException e) {
        return Response.of(e.getConstraintViolations());
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleBusinessLogicException(BusinessLogicException e) {
        final Response response = Response.of(e.getExceptionCode());
        return ResponseEntity.status(HttpStatus.valueOf(e.getExceptionCode().getStatus())).body(response);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Response handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return Response.of(HttpStatus.METHOD_NOT_ALLOWED,"메서드 문법이 틀렸습니다. 문법을 지켜주세요.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        return Response.of(HttpStatus.BAD_REQUEST,
                "정확한 제이슨 요청을 부탁드립니다.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        return Response.of(HttpStatus.BAD_REQUEST,
                "파라미터가 빠졌습니다.");
    }
}
