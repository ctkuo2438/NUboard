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
    // General Errors (1xxx)
    /**
     * Represents an unknown error that occurs during processing.
     * HttpStatus 500 Internal Server Error.
     */
    // UNKNOWN_ERROR(1000, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * Indicates that the provided input is invalid or missing.
     * HttpStatus 400 Bad Request.
     */
    // INVALID_INPUT(1001, "Invalid or missing input", HttpStatus.BAD_REQUEST),

    // User Errors (2xxx)
    /**
     * Indicates that the specified user was not found.
     * HttpStatus 404 Not Found.
     */
    // USER_NOT_FOUND(2001, "User not found", HttpStatus.NOT_FOUND),

    // Event Errors (3xxx)
    /**
     * Indicates that the specified event was not found.
     * HttpStatus 404 Not Found.
     */
    // EVENT_NOT_FOUND(3001, "Event not found", HttpStatus.NOT_FOUND),
    /**
     * Indicates that the time range for querying events is invalid (e.g., start time is after end time).
     * HttpStatus 400 Bad Request.
     */
    // INVALID_TIME_RANGE(3002, "Start time must be before end time", HttpStatus.BAD_REQUEST),

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
