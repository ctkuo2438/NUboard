package com.neu.nuboard.controller;

import com.neu.nuboard.dto.EventRegistrationDTO;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.service.EventRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling HTTP requests related to event registrations.
 * Base path: /api/registrations
 */
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@RestController
@RequestMapping("/api/registrations")
public class EventRegistrationController {

    private final EventRegistrationService registrationService;

    /**
     * Constructs an EventRegistrationController with the specified EventRegistrationService.
     *
     * @param registrationService the service to handle event-registration-related business logic
     */
    public EventRegistrationController(EventRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Registers a user for an event.
     *
     * @param eventId the ID of the event to register for
     * @param userId the ID of the user to register
     * @return a ResponseEntity containing a {@link SuccessResponse} with an {@link EventRegistrationDTO}
     *         containing the event ID and user ID, and HTTP status 201 (Created)
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<EventRegistrationDTO>> registerForEvent(
            @RequestParam("eventId") String eventId,
            @RequestParam("userId") String userId) {
        registrationService.registerForEvent(eventId, userId);
        EventRegistrationDTO responseDTO = new EventRegistrationDTO(null, eventId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(responseDTO));
    }

    /**
     * Unregisters a user from an event.
     *
     * @param eventId the ID of the event to unregister from
     * @param userId the ID of the user to unregister
     * @return a ResponseEntity containing a {@link SuccessResponse} with an {@link EventRegistrationDTO}
     *         containing the event ID and user ID, and HTTP status 200 (OK)
     */
    @DeleteMapping("/unregister")
    public ResponseEntity<SuccessResponse<EventRegistrationDTO>> unregisterForEvent(
            @RequestParam("eventId") String eventId,
            @RequestParam("userId") String userId) {
        registrationService.unregisterForEvent(eventId, userId);
        EventRegistrationDTO responseDTO = new EventRegistrationDTO(null, eventId, userId);
        return ResponseEntity.ok(new SuccessResponse<>(responseDTO));
    }

    /**
     * Retrieves all registrations for a specific event.
     *
     * @param eventId the ID of the event
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of registrations for the specified event and HTTP status 200 (OK)
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<SuccessResponse<List<EventRegistrationDTO>>> getRegistrationsByEventId(@PathVariable String eventId) {
        List<EventRegistrationDTO> registrations = registrationService.getRegistrationsByEventId(eventId);
        return ResponseEntity.ok(new SuccessResponse<>(registrations));
    }

    /**
     * Retrieves all registrations for a specific user.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of registrations for the specified user and HTTP status 200 (OK)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<SuccessResponse<List<EventRegistrationDTO>>> getRegistrationsByUserId(@PathVariable String userId) {
        List<EventRegistrationDTO> registrations = registrationService.getRegistrationsByUserId(userId);
        return ResponseEntity.ok(new SuccessResponse<>(registrations));
    }

    /**
     * Retrieves all event registrations.
     *
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of all registrations and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<List<EventRegistrationDTO>>> getAllRegistrations() {
        List<EventRegistrationDTO> registrations = registrationService.getAllRegistrations();
        return ResponseEntity.ok(new SuccessResponse<>(registrations));
    }
}