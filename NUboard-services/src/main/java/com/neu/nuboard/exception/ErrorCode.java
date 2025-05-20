package com.neu.nuboard.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // General Error 1xxx
    UNKNOWN_ERROR(1000, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),
    PARAM_ERROR(1001, "Invalid Parameter", HttpStatus.BAD_REQUEST),

    // User Error 2xxx
    USER_ALREADY_EXISTS(2001, "User Already Exists", HttpStatus.CONFLICT);

    // Event Error 3xxx

    // Registration Error 4xxx

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getHttpStatus() { return httpStatus; }
}
