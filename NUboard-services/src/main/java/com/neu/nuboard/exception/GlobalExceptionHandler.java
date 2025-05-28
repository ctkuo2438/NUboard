package com.neu.nuboard.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class centralizes the handling of exceptions thrown during request processing,
 * ensuring that all error responses follow a consistent format.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link BusinessException} thrown during request processing.
     * Converts the exception into a standardized error response with the associated error code, message, and HTTP status.
     *
     * @param ex the BusinessException to handle
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        Map<String, Object> body = new HashMap<>();
        body.put("code", errorCode.getCode());
        body.put("message", ex.getMessage());
        body.put("data", null);

        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    /**
     * Handles unexpected exceptions (e.g., runtime exceptions) that are not explicitly caught elsewhere.
     * Converts the exception into a standardized error response with a generic "Unknown Error" message.
     *
     * @param ex the unexpected exception to handle
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOtherExceptions(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ErrorCode.UNKNOWN_ERROR.getCode());
        body.put("message", ErrorCode.UNKNOWN_ERROR.getMessage());
        body.put("data", null);

        return ResponseEntity.status(ErrorCode.UNKNOWN_ERROR.getHttpStatus()).body(body);
    }
}