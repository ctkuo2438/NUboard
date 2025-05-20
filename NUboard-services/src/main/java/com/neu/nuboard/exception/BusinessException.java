package com.neu.nuboard.exception;

/* How to use?
 * throw new BusinessException(ErrorCode.SPECIFIC_ERROR_CODE);
 */
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
