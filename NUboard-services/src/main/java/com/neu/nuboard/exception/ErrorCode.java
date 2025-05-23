package com.neu.nuboard.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // General Error 1xxx
    UNKNOWN_ERROR(1000, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),
    PARAM_ERROR(1001, "Invalid Parameter", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(1002, "Validation Error", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(1003, "Resource Not Found", HttpStatus.NOT_FOUND),
    DATABASE_ERROR(1004, "Database Operation Failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // Event Error 3xxx
    EVENT_NOT_FOUND(3001, "Event Not Found", HttpStatus.NOT_FOUND),
    EVENT_INVALID_TIME(3002, "Invalid Event Time", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_TITLE(3003, "Invalid Event Title", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_LOCATION(3004, "Invalid Event Location", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_ADDRESS(3005, "Invalid Event Address", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_CREATOR(3006, "Invalid Event Creator", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_ORGANIZER(3007, "Invalid Event Organizer", HttpStatus.BAD_REQUEST),
    EVENT_ALREADY_EXISTS(3008, "Event Already Exists", HttpStatus.CONFLICT),
    EVENT_PARTICIPANT_ALREADY_REGISTERED(3009, "Participant Already Registered for Event", HttpStatus.CONFLICT),
    EVENT_PARTICIPANT_INVALID(3010, "Invalid Participant", HttpStatus.BAD_REQUEST);

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
