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
    USER_ALREADY_EXISTS(2001, "Username Already Exists", HttpStatus.CONFLICT),
    EMPTY_USERNAME(2002, "Username cannot be empty", HttpStatus.NOT_FOUND),
    OVER_MAX_LENGTH(2003, "Username Can't Be Over 255 Characters", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(2004, "User not found", HttpStatus.NOT_FOUND),
    USER_QUERY_FAILED(2015, "Failed to query users from database", HttpStatus.INTERNAL_SERVER_ERROR),

    EMAIL_ALREADY_EXISTS(2005, "Email Already Exists", HttpStatus.CONFLICT),
    EMPTY_EMAIL(2006, "Email cannot be empty", HttpStatus.NOT_FOUND),
    OVER_MAX_LENGHT_EMAIL(2007, "Email exceeds maximum length", HttpStatus.NOT_FOUND),
    INVALID_EMAIL(2008, "Invalid email format", HttpStatus.NOT_FOUND),

    USER_EMPTY_PROGRAM(2009, "Program cannot be empty", HttpStatus.NOT_FOUND),
    USER_INVALID_PROGRAM(2010, "Invalid program selection. Please select from the provided options", HttpStatus.NOT_FOUND),

    USER_INVALID_LOCATION_SELECTION(2011, "Invalid location selection. Please select from the provided options", HttpStatus.BAD_REQUEST),
    USER_SEARCH_KEYWORD_EMPTY(2012, "Search keyword cannot be empty", HttpStatus.BAD_REQUEST),
    USER_SEARCH_NO_RESULTS(2013, "No users found matching the search criteria", HttpStatus.NOT_FOUND),
    USER_SEARCH_INVALID_KEYWORD(2014, "Invalid search keyword format", HttpStatus.BAD_REQUEST),


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
