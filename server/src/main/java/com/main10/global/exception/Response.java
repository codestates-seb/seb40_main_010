package com.main10.global.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Response {
    private int status;
    private String message;
    private List<Field> fieldError;
    private List<ConstraintViolation> violationErrors;

    public Response(List<Field> fieldError, List<ConstraintViolation> violationErrors) {
        this.fieldError = fieldError;
        this.violationErrors = violationErrors;
    }

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static Response of(Set<javax.validation.ConstraintViolation<?>> violations) {
        return new Response(null, ConstraintViolation.of(violations));
    }

    public static Response of(BindingResult bindingResult) {
        return new Response(Field.of(bindingResult), null);
    }

    public static Response of(ExceptionCode exceptionCode) {
        return new Response(exceptionCode.getStatus(), exceptionCode.getMessage());
    }


    public static Response of(HttpStatus httpStatus, String message) {
        return new Response(httpStatus.value(), message);
    }


    @Getter
    public static class Field {
        private String field;
        private Object rejectedValue;
        private String reason;

        private Field(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<Field> of(BindingResult bindingResult) {
            final List<FieldError> fieldErrors =
                    bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new Field(
                            error.getField(),
                            error.getRejectedValue() == null ?
                                    "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
    @Getter
    public static class ConstraintViolation {
        private String constraintViolation;
        private String rejectedValue;
        private String reason;

        public ConstraintViolation(String constraintViolation, String rejectedValue, String reason) {
            this.constraintViolation = constraintViolation;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<ConstraintViolation> of(Set<javax.validation.ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(error -> new ConstraintViolation(
                            error.getPropertyPath().toString(),
                            error.getInvalidValue().toString(),
                            error.getMessage()

                    )).collect(Collectors.toList());
        }
    }
}