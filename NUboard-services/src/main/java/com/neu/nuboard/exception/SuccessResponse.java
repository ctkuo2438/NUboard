package com.neu.nuboard.exception;

/* How to use?
 * Method1: return new SuccessResponse<>(user);
 * Method2: return new SuccessResponse<>();
 * (Default HTTP status code is 200)
 */
public class SuccessResponse<T> {
    private final int code; // Code 0 represent success, while any non-zero indicates a business error.
    private final String message;
    private final T data;

    public SuccessResponse(){
        this.code = 0;
        this.message = "Success";
        this.data = null;
    }

    public SuccessResponse(T data){
        this.code = 0;
        this.message = "Success";
        this.data = data;
    }

    // Getter
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}