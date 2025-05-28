package com.neu.nuboard.exception;

/**
 * A generic response class used to represent successful API responses.
 * This class standardizes the success response format with a code, message, and optional data payload.
 * The default HTTP status code is 200 (OK).
 * <p>
 * Usage examples:
 * <pre>
 * // With data
 * return new SuccessResponse<>(user);
 * // Without data
 * return new SuccessResponse<>();
 * </pre>
 *
 * @param <T> the type of the data payload
 */
public class SuccessResponse<T> {
    private final int code; // Code 0 represent success, while any non-zero indicates a business error.
    private final String message;
    private final T data;

    /**
     * Constructs a SuccessResponse with no data.
     * The default code is 0, and the message is "Success".
     */
    public SuccessResponse(){
        this.code = 0;
        this.message = "Success";
        this.data = null;
    }

    /**
     * Constructs a SuccessResponse with the specified data.
     * The default code is 0, and the message is "Success".
     *
     * @param data the data payload to include in the response
     */
    public SuccessResponse(T data){
        this.code = 0;
        this.message = "Success";
        this.data = data;
    }

    /**
     * Gets the response code.
     *
     * @return the response code (0 for success)
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the response message.
     *
     * @return the response message ("Success")
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the data payload.
     * Frontend could call getData() to retrieve the data.
     *
     * @return the data payload, or null if none was provided
     */
    public T getData() {
        return data;
    }
}
