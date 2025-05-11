package com.neu.nuboard.exception;

public class EventNotFoundException extends RuntimeException {

    /**
     * Constructor for EventNotFoundException.
     * @param message The message to display when the exception is thrown.
     */
    public EventNotFoundException(String message) {
        super(message);
    }
}
