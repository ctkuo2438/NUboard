package com.neu.nuboard.controller;

import com.neu.nuboard.dto.EventRegistrationDTO;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.model.EventRegistration;
import com.neu.nuboard.service.EventRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * Admin endpoint for managing registrations.
     * Users can only register themselves unless they have admin privileges.
     *
     * @param eventId the ID of the event to register for
     * @param userId the ID of the user to register
     * @return a ResponseEntity containing a {@link SuccessResponse} with an {@link EventRegistrationDTO}
     * containing the event ID and user ID, and HTTP status 201 (Created)
     */
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('EVENT_REGISTER') and (hasRole('ADMIN'))")
    public ResponseEntity<SuccessResponse<EventRegistrationDTO>> registerForEvent(
            @RequestParam("eventId") String eventId,
            @RequestParam("userId") Long userId) {
        EventRegistration registration = registrationService.registerForEvent(eventId, userId);
        EventRegistrationDTO responseDTO = new EventRegistrationDTO(
                registration.getId(),
                eventId,
                userId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(responseDTO));
    }

    /**
     * Registers a specific user for an event by userId.
     * Endpoint for USER to register for events.
     *
     * @param eventId the ID of the event to register for
     * @param userId the ID of the user to register
     * @return a ResponseEntity containing registration details
     */
    @PostMapping("/register/{eventId}/{userId}")
    @PreAuthorize("hasAuthority('EVENT_REGISTER')")
    public ResponseEntity<SuccessResponse<EventRegistrationDTO>> registerUserForEvent(
            @PathVariable String eventId,
            @PathVariable Long userId) {
        EventRegistration registration = registrationService.registerForEvent(eventId, userId);
        EventRegistrationDTO responseDTO = new EventRegistrationDTO(
                registration.getId(),
                eventId,
                userId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(responseDTO));
    }

    /**
     * Unregisters a user from an event.
     * Users can only unregister themselves unless they have admin privileges.
     *
     * @param eventId the ID of the event to unregister from
     * @param userId the ID of the user to unregister
     * @return a ResponseEntity containing a {@link SuccessResponse} with an {@link EventRegistrationDTO}
     * containing the event ID and user ID, and HTTP status 200 (OK)
     */
    @DeleteMapping("/unregister")
    @PreAuthorize("hasAuthority('EVENT_UNREGISTER') and (hasRole('ADMIN'))")
    public ResponseEntity<SuccessResponse<EventRegistrationDTO>> unregisterForEvent(
            @RequestParam("eventId") String eventId,
            @RequestParam("userId") Long userId) {
        EventRegistration registration = registrationService.unregisterForEvent(eventId, userId);
        EventRegistrationDTO responseDTO = new EventRegistrationDTO(
                registration.getId(),
                eventId,
                userId
        );
        return ResponseEntity.ok(new SuccessResponse<>(responseDTO));
    }

    /**
     * Unregisters a specific user from an event.
     * Endpoint for USER to unregister from events.
     *
     * @param eventId the ID of the event to unregister from
     * @param userId the ID of the user to unregister
     * @return a ResponseEntity containing unregistration details
     */
    @DeleteMapping("/unregister/{eventId}/{userId}")
    @PreAuthorize("hasAuthority('EVENT_UNREGISTER')")
    public ResponseEntity<SuccessResponse<EventRegistrationDTO>> unregisterUserFromEvent(
            @PathVariable String eventId,
            @PathVariable Long userId) {
        EventRegistration registration = registrationService.unregisterForEvent(eventId, userId);
        EventRegistrationDTO responseDTO = new EventRegistrationDTO(
                registration.getId(),
                eventId,
                userId
        );
        return ResponseEntity.ok(new SuccessResponse<>(responseDTO));
    }

    /**
     * Retrieves all registrations for a specific event.
     * Only admins can view all registrations for an event.
     *
     * @param eventId the ID of the event
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of registrations for the specified event and HTTP status 200 (OK)
     */
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAuthority('REGISTRATION_VIEW')")
    public ResponseEntity<SuccessResponse<List<EventRegistrationDTO>>> getRegistrationsByEventId(@PathVariable String eventId) {
        List<EventRegistrationDTO> registrations = registrationService.getRegistrationsByEventId(eventId);
        return ResponseEntity.ok(new SuccessResponse<>(registrations));
    }

    /**
     * Retrieves all registrations for a specific user.
     * Users can view their own registrations, admins can view all.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of registrations for the specified user and HTTP status 200 (OK)
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('EVENT_VIEW')")
    public ResponseEntity<SuccessResponse<List<EventRegistrationDTO>>> getRegistrationsByUserId(@PathVariable Long userId) {
        List<EventRegistrationDTO> registrations = registrationService.getRegistrationsByUserId(userId);
        return ResponseEntity.ok(new SuccessResponse<>(registrations));
    }

    /**
     * Retrieves all event registrations.
     * Only admins can view all registrations in the system.
     *
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of all registrations and HTTP status 200 (OK)
     */
    @GetMapping
    @PreAuthorize("hasAuthority('REGISTRATION_MANAGE')")
    public ResponseEntity<SuccessResponse<List<EventRegistrationDTO>>> getAllRegistrations() {
        List<EventRegistrationDTO> registrations = registrationService.getAllRegistrations();
        return ResponseEntity.ok(new SuccessResponse<>(registrations));
    }
}