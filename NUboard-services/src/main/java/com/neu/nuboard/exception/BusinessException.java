package com.neu.nuboard.exception;

/* How to use?
 * if you need a separate ErrorCode, you can use Method 1.
 * Method1: throw new BusinessException(ErrorCode.SPECIFIC_ERROR_CODE);
 *
 * if you want to avoid defining too many ErrorCodes and prefer to reuse a general ErrorCode with different messages,
 * you can pass a custom message along with a general ErrorCode as Method 2.
 * Method2: throw new BusinessException(ErrorCode.SPECIFIC_ERROR_CODE, "message");
 */
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        message = null;
        this.errorCode = errorCode;
    }

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
