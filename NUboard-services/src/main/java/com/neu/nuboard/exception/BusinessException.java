package com.neu.nuboard.exception;

/**
 * Custom exception class for handling business logic errors in the application.
 * This exception is thrown when a business rule is violated, and it carries an associated {@link ErrorCode}.
 * <p>
 * Usage example:
 * <pre>
 * throw new BusinessException(ErrorCode.SPECIFIC_ERROR_CODE);
 * </pre>
 */
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    /**
     * Constructs a new BusinessException with the specified error code.
     * RuntimeException has a column message to store error messages.
     *
     * @param errorCode the error code associated with this exception
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
