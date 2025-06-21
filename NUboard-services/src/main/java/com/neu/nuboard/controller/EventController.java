package com.neu.nuboard.controller;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param eventCreateDTO The DTO containing event details.
     * @return The created event details wrapped in SuccessResponse.
     */
    @PostMapping
    public ResponseEntity<SuccessResponse<EventResponseDTO>> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO responseDTO = eventService.createEvent(eventCreateDTO);
        return new ResponseEntity<>(new SuccessResponse<>(responseDTO), HttpStatus.CREATED);
    }

    /**
     * Retrieves all events.
     * @return List of all events wrapped in SuccessResponse.
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }

    /**
     * Searches events by keyword.
     * @param keyword The search keyword.
     * @return List of matching events wrapped in SuccessResponse.
     */
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<EventResponseDTO>>> searchEvents(@RequestParam String keyword) {
        List<EventResponseDTO> events = eventService.searchEvents(keyword);
        return ResponseEntity.ok(new SuccessResponse<>(events));
    }

    /**
     * Updates an existing event.
     * @param id The ID of the event to update.
     * @param eventCreateDTO The DTO containing updated event details.
     * @return The updated event details wrapped in SuccessResponse.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<EventResponseDTO>> updateEvent(
            @PathVariable String id,
            @Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, eventCreateDTO);
        return ResponseEntity.ok(new SuccessResponse<>(updatedEvent));
    }

    /**
     * Deletes an event.
     * @param id The ID of the event to delete.
     * @return SuccessResponse with no data.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new SuccessResponse<>());
    }

}