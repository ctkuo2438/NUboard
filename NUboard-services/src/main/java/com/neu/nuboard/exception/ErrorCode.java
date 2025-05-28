package com.neu.nuboard.exception;

import org.springframework.http.HttpStatus;

/**
 * Enum defining error codes, messages, and corresponding HTTP statuses for various exceptions in the application.
 * Error codes are categorized as follows:
 * - General Errors: 1xxx
 * - User Errors: 2xxx
 * - Event Errors: 3xxx
 * - Registration Errors: 4xxx
 */
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
    
    USER_EMPTY_PROGRAM(2009, "Program cannot be empty", HttpStatus.NOT_FOUND),
    USER_INVALID_PROGRAM(2010, "Invalid program selection. Please select from the provided options", HttpStatus.NOT_FOUND),
    
    USER_INVALID_LOCATION_SELECTION(2011, "Invalid location selection. Please select from the provided options", HttpStatus.BAD_REQUEST),
    USER_SEARCH_KEYWORD_EMPTY(2012, "Search keyword cannot be empty", HttpStatus.BAD_REQUEST),
    USER_SEARCH_NO_RESULTS(2013, "No users found matching the search criteria", HttpStatus.NOT_FOUND),
    USER_SEARCH_INVALID_KEYWORD(2014, "Invalid search keyword format", HttpStatus.BAD_REQUEST),

    // Event Error 3xxx
    
    // Registration Error 4xxx
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    /**
     * Constructs an ErrorCode with the specified code, message, and HTTP status.
     *
     * @param code the unique error code
     * @param message the error message describing the issue
     * @param httpStatus the corresponding HTTP status
     */
    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets the error code.
     *
     * @return the error code, 1xxx, 2xxx, 3xxx, 4xxx
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the corresponding HTTP status.
     *
     * @return the HTTP status
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
