package com.neu.nuboard.exception;

import org.springframework.http.HttpStatus;

/**
 * Enumeration of error codes used throughout the application.
 * Error codes are organized by category with specific ranges:
 * - 1xxx: General/System errors
 * - 2xxx: User-related errors
 * - 3xxx: Event-related errors
 * - 4xxx: Registration-related errors
 * - 5xxx: Admin-related errors
 */

public enum ErrorCode {
    // General Error (1xxx)
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

    // Event Error (3xxx)
    EVENT_NOT_FOUND(3001, "Event Not Found", HttpStatus.NOT_FOUND),
    EVENT_INVALID_TIME(3002, "Invalid Event Time", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_TITLE(3003, "Invalid Event Title", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_LOCATION(3004, "Invalid Event Location", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_ADDRESS(3005, "Invalid Event Address", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_CREATOR(3006, "Invalid Event Creator", HttpStatus.BAD_REQUEST),
    EVENT_INVALID_ORGANIZER(3007, "Invalid Event Organizer", HttpStatus.BAD_REQUEST),

    // Registration Errors (4xxx)
    ALREADY_REGISTERED(4001, "User already registered", HttpStatus.CONFLICT),
    REGISTRATION_NOT_FOUND(4002, "Registration not found", HttpStatus.NOT_FOUND),
    REGISTRATION_FAILED(4003, "Registration failed", HttpStatus.INTERNAL_SERVER_ERROR),
    UNREGISTRATION_FAILED(4004, "Unregistration failed", HttpStatus.INTERNAL_SERVER_ERROR),
    REGISTRATION_DEADLINE_PASSED(4005, "Registration deadline has passed", HttpStatus.BAD_REQUEST),
    REGISTRATION_NOT_OPEN(4006, "Registration is not open for this event", HttpStatus.BAD_REQUEST),

    // Admin related Errors (5xxx)
    INVALID_ROLE_NAME(5001, "Invalid role name provided", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(5002, "Role not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_HAS_ROLE(5008, "User already has this role", HttpStatus.CONFLICT),
    CANNOT_REMOVE_LAST_ADMIN(5010, "Cannot remove the last admin role from the system", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_DISABLE_OWN_ACCOUNT(5011, "Cannot disable your own account", HttpStatus.UNPROCESSABLE_ENTITY);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    /**
     * Constructor for ErrorCode enum.
     *
     * @param code the numeric error code
     * @param message the error message description
     * @param httpStatus the corresponding HTTP status code
     */
    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getHttpStatus() { return httpStatus; }
}
