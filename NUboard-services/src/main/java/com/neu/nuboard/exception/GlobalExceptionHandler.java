package com.neu.nuboard.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        Map<String, Object> body = new HashMap<>();
        body.put("code", errorCode.getCode());
        body.put("message", ex.getMessage());
        body.put("data", null);

        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    // Exception class is a built-in class in Java
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOtherExceptions(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ErrorCode.UNKNOWN_ERROR.getCode());
        body.put("message", ErrorCode.UNKNOWN_ERROR.getMessage());
        body.put("data", null);

        return ResponseEntity.status(ErrorCode.UNKNOWN_ERROR.getHttpStatus()).body(body);
    }
}
