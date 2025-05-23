package com.neu.nuboard.controller;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.model.OrganizerType;
import com.neu.nuboard.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for handling HTTP requests related to events.
 * Base path: /api/events
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    /**
     * Constructs an EventController with the specified EventService.
     *
     * @param eventService the service to handle event-related business logic
     */
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates a new event.
     *
     * @param eventCreateDTO the DTO containing the event details
     * @return a ResponseEntity containing a {@link SuccessResponse} with the created event details and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<SuccessResponse<EventResponseDTO>> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO createdEvent = eventService.createEvent(eventCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(createdEvent));
    }

    /**
     * Retrieves all events.
     *
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of all events and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the ID of the event to retrieve
     * @return a ResponseEntity containing a {@link SuccessResponse} with the event details and HTTP status 200 (OK)
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<SuccessResponse<EventResponseDTO>> getEventById(@PathVariable String eventId) {
        EventResponseDTO event = eventService.getEventById(eventId);
        return ResponseEntity.ok(new SuccessResponse<>(event));
    }

    /**
     * Retrieves all events created by a specific creator.
     *
     * @param creatorId the ID of the creator
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of events created by the specified creator and HTTP status 200 (OK)
     */
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> getEventsByCreatorId(@PathVariable String creatorId) {
        List<EventResponseDTO> events = eventService.getEventsByCreatorId(creatorId);
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }

    /**
     * Retrieves all events of a specific organizer type.
     *
     * @param organizerType the type of organizer (e.g., SCHOOL or CORPORATE)
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of events of the specified organizer type and HTTP status 200 (OK)
     */
    @GetMapping("/organizer-type/{organizerType}")
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> getEventsByOrganizerType(@PathVariable OrganizerType organizerType) {
        List<EventResponseDTO> events = eventService.getEventsByOrganizerType(organizerType);
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }

    /**
     * Retrieves all events that start within a given time range.
     *
     * @param start the start time (format: yyyy-MM-dd'T'HH:mm:ss, e.g., 2025-05-16T14:00:00)
     * @param end the end time (format: yyyy-MM-dd'T'HH:mm:ss, e.g., 2025-05-16T15:00:00)
     * @return a ResponseEntity containing a {@link SuccessResponse} with a list of events that start within the specified time range and HTTP status 200 (OK)
     */
    @GetMapping("/time-range")
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> getEventsByStartTimeBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<EventResponseDTO> events = eventService.getEventsByStartTimeBetween(start, end);
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }
}