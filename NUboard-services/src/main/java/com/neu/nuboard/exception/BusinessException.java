package com.neu.nuboard.exception;

/* Custom exception class for handling business logic errors in the application.
 * This exception is thrown when a business rule is violated, and it carries an associated {@link ErrorCode}.
 *
 * Common Usage:
 * 1. if you need a separate ErrorCode, you can use Method 1.
 *    Method1: throw new BusinessException(ErrorCode.SPECIFIC_ERROR_CODE);
 * 2. if you want to avoid defining too many ErrorCodes and prefer to reuse a general ErrorCode with different messages,
 * you can pass a custom message along with a general ErrorCode as Method 2.
 *    Method2: throw new BusinessException(ErrorCode.SPECIFIC_ERROR_CODE, "message");
 */
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    /**
     * Constructs a new BusinessException with the specified error code.
     * RuntimeException has a column message to store error messages.
     *
     * @param errorCode the error code associated with this exception
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        message = null;
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new BusinessException with a general error code and a specified message.
     * RuntimeException has a column message to store error messages.
     *
     * @param errorCode the general error code associated with this exception.
     *                  we don't pass message associated with this general error code to the front end.
     * @param message the specified message used in the exception, and will be passed to the front end.
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = message;
    }

    // Getter
    @Override
    public String getMessage() {
        return message != null ? message : errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
