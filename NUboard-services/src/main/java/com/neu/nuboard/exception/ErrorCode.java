package com.neu.nuboard.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // General Error 1xxx
    UNKNOWN_ERROR(1000, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),
    PARAM_ERROR(1001, "Invalid Parameter", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(1002, "Validation Error", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(1003, "Resource Not Found", HttpStatus.NOT_FOUND),
    DATABASE_ERROR(1004, "Database Operation Failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // User Errors (2xxx)
    USER_NOT_FOUND(2001, "User not found", HttpStatus.NOT_FOUND),

    // Event Error 3xxx
    EVENT_NOT_FOUND(3001, "Event Not Found", HttpStatus.NOT_FOUND),
    EVENT_INVALID_TIME(3002, "Invalid Event Time", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_TITLE(3003, "Invalid Event Title", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_LOCATION(3004, "Invalid Event Location", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_ADDRESS(3005, "Invalid Event Address", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_CREATOR(3006, "Invalid Event Creator", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_ORGANIZER(3007, "Invalid Event Organizer", HttpStatus.BAD_REQUEST),


    // Registration Errors (4xxx)
    /**
     * Indicates that the user is already registered for the event.
     * HttpStatus 409 Conflict.
     */
    ALREADY_REGISTERED(4001, "User already registered", HttpStatus.CONFLICT),
    /**
     * Indicates that the registration record for the specified event and user was not found.
     * HttpStatus 404 Not Found.
     */
    REGISTRATION_NOT_FOUND(4002, "Registration not found", HttpStatus.NOT_FOUND);

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
