package com.neu.nuboard.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.PersistenceException;

/**
 * Global exception handler for handling API errors.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles EventNotFoundException, returning a 404 Not Found response.
     * @param ex The EventNotFoundException.
     * @return ResponseEntity with an error message and 404 status.
     */
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<String> handleEventNotFoundException(EventNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles IllegalArgumentException, returning a 400 Bad Request response.
     * @param ex The IllegalArgumentException.
     * @return ResponseEntity with an error message and 400 status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors from @Valid annotations, returning a 400 Bad Request response.
     * @param ex The MethodArgumentNotValidException.
     * @return ResponseEntity with the first validation error message and 400 status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DataAccessException (e.g., database errors), returning a 400 Bad Request response.
     * @param ex The DataAccessException.
     * @return ResponseEntity with an error message and 400 status.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        return new ResponseEntity<>("Database error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles PersistenceException (e.g., JPA errors), returning a 400 Bad Request response.
     * @param ex The PersistenceException.
     * @return ResponseEntity with an error message and 400 status.
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<String> handlePersistenceException(PersistenceException ex) {
        return new ResponseEntity<>("Persistence error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles JsonParseException (e.g., invalid JSON format), returning a 400 Bad Request response.
     * @param ex The JsonParseException.
     * @return ResponseEntity with an error message and 400 status.
     */
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> handleJsonParseException(JsonParseException ex) {
        return new ResponseEntity<>("Invalid JSON format: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles JsonMappingException (e.g., JSON mapping errors), returning a 400 Bad Request response.
     * @param ex The JsonMappingException.
     * @return ResponseEntity with an error message and 400 status.
     */
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<String> handleJsonMappingException(JsonMappingException ex) {
        return new ResponseEntity<>("JSON mapping error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unexpected exceptions, returning a 500 Internal Server Error response.
     * @param ex The Exception.
     * @return ResponseEntity with a detailed error message and 500 status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}