package com.neu.nuboard.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        Map<String, Object> body = new HashMap<>();
        body.put("code", errorCode.getCode());
        body.put("message", errorCode.getMessage());
        body.put("data", null);

        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"
                ));

        Map<String, Object> body = new HashMap<>();
        body.put("code", ErrorCode.VALIDATION_ERROR.getCode());
        body.put("message", ErrorCode.VALIDATION_ERROR.getMessage());
        body.put("errors", errors);
        body.put("data", null);

        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getHttpStatus()).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ErrorCode.DATABASE_ERROR.getCode());
        body.put("message", "Database constraint violation: " + ex.getMessage());
        body.put("data", null);

        return ResponseEntity.status(ErrorCode.DATABASE_ERROR.getHttpStatus()).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ErrorCode.PARAM_ERROR.getCode());
        body.put("message", ex.getMessage());
        body.put("data", null);

        return ResponseEntity.status(ErrorCode.PARAM_ERROR.getHttpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOtherExceptions(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ErrorCode.UNKNOWN_ERROR.getCode());
        body.put("message", ErrorCode.UNKNOWN_ERROR.getMessage());
        body.put("data", null);

        return ResponseEntity.status(ErrorCode.UNKNOWN_ERROR.getHttpStatus()).body(body);
    }
}
