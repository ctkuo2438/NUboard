package com.neu.nuboard.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // General Error 1xxx
    UNKNOWN_ERROR(1000, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),
    PARAM_ERROR(1001, "Invalid Parameter", HttpStatus.BAD_REQUEST),

    // User Error 2xxx
    USER_ALREADY_EXISTS(2001, "Username Already Exists", HttpStatus.CONFLICT),
    EMPTY_USERNAME(2002, "Username cannot be empty", HttpStatus.NOT_FOUND),
    OVER_MAX_LENGTH(2003, "Username Can't Be Over 255 Characters", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(2004, "User not found", HttpStatus.NOT_FOUND),
    USER_QUERY_FAILED(2015, "Failed to query users from database", HttpStatus.INTERNAL_SERVER_ERROR),

    EMAIL_ALREADY_EXISTS(2005, "Email Already Exists", HttpStatus.CONFLICT),
    EMPTY_EMAIL(2006, "Email cannot be empty", HttpStatus.NOT_FOUND),
    OVER_MAX_LENGHT_EMAIL(2007, "Email exceeds maximum length", HttpStatus.NOT_FOUND),
    INVALID_EMAIL(2008, "Invalid email format", HttpStatus.NOT_FOUND),
    
    EMPTY_PROGRAM(2009, "Program cannot be empty", HttpStatus.NOT_FOUND),
    INVALID_PROGRAM(2010, "Invalid program selection. Please select from the provided options", HttpStatus.NOT_FOUND),
    
    INVALID_LOCATION(2011, "Invalid location selection. Please select from the provided options", HttpStatus.BAD_REQUEST),
    
    // Search Error 2xxx
    SEARCH_KEYWORD_EMPTY(2012, "Search keyword cannot be empty", HttpStatus.BAD_REQUEST),
    SEARCH_NO_RESULTS(2013, "No users found matching the search criteria", HttpStatus.NOT_FOUND),
    SEARCH_INVALID_KEYWORD(2014, "Invalid search keyword format", HttpStatus.BAD_REQUEST),

    // Event Error 3xxx
    EVENT_NOT_FOUND(3001, "Event not found", HttpStatus.NOT_FOUND),
    EMPTY_TITLE(3002, "Title cannot be empty", HttpStatus.BAD_REQUEST),
    EMPTY_EVENT_DATE(3003, "Event date cannot be empty", HttpStatus.BAD_REQUEST),
    INVALID_EVENT_DATE(3004, "Event date cannot be in the past", HttpStatus.BAD_REQUEST),
    EMPTY_CREATOR_ID(3005, "Creator ID cannot be empty", HttpStatus.BAD_REQUEST),
    USER_ALREADY_REGISTERED(3006, "User is already registered for this event", HttpStatus.CONFLICT),

    // Registration Error 4xxx
    ;

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