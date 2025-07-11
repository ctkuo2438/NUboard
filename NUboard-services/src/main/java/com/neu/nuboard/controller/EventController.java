package com.neu.nuboard.controller;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for managing events.
 */
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates a new event.
     * ADMIN and USER users can create events.
     * @param eventCreateDTO The DTO containing event details.
     * @return The created event details wrapped in SuccessResponse.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('EVENT_CREATE')")
    public ResponseEntity<SuccessResponse<EventResponseDTO>> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO responseDTO = eventService.createEvent(eventCreateDTO);
        return new ResponseEntity<>(new SuccessResponse<>(responseDTO), HttpStatus.CREATED);
    }

    /**
     * Retrieves all events.
     * All authenticated users can view events.
     * @return List of all events wrapped in SuccessResponse.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('EVENT_VIEW')")
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }

    /**
     * Searches events by keyword.
     * All authenticated users can search events.
     * @param keyword The search keyword.
     * @return List of matching events wrapped in SuccessResponse.
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('EVENT_VIEW')")
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> searchEvents(@RequestParam String keyword) {
        List<EventResponseDTO> events = eventService.searchEvents(keyword);
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }

    /**
     * Updates an existing event.
     * Only ADMIN users can update events.
     * @param id The ID of the event to update.
     * @param eventCreateDTO The DTO containing updated event details.
     * @return The updated event details wrapped in SuccessResponse.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_UPDATE')")
    public ResponseEntity<SuccessResponse<EventResponseDTO>> updateEvent(
            @PathVariable String id,
            @Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, eventCreateDTO);
        return ResponseEntity.ok(new SuccessResponse<>(updatedEvent));
    }

    /**
     * Deletes an event.
     * Only ADMIN users can delete events.
     * @param id The ID of the event to delete.
     * @return SuccessResponse with no data.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_DELETE')")
    public ResponseEntity<SuccessResponse<Void>> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new SuccessResponse<Void>(null));
    }

    /**
     * Gets events created by a specific user.
     * @param creatorId The ID of the creator
     * @return List of events created by the user.
     */
    @GetMapping("/by-creator/{creatorId}")
    @PreAuthorize("hasAuthority('EVENT_VIEW')")
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> getEventsByCreator(@PathVariable String creatorId) {
        List<EventResponseDTO> events = eventService.getEventsByCreatorId(creatorId);
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }
}