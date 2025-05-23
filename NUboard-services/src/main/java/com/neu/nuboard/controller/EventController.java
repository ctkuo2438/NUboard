package com.neu.nuboard.controller;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

/**
 * REST controller for managing events.
 */
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
     * @return The created event details.
     */
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO responseDTO = eventService.createEvent(eventCreateDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves all events.
     * @return List of all events.
     */
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Searches events by keyword.
     * @param keyword The search keyword.
     * @return List of matching events.
     */
    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDTO>> searchEvents(@RequestParam String keyword) {
        List<EventResponseDTO> events = eventService.searchEvents(keyword);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Updates an existing event.
     * @param id The ID of the event to update.
     * @param eventCreateDTO The DTO containing updated event details.
     * @return The updated event details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, eventCreateDTO);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    /**
     * Deletes an event.
     * @param id The ID of the event to delete.
     * @return No content response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Registers a participant for an event.
     * @param eventId The ID of the event.
     * @param username The username of the participant.
     * @return The updated event details.
     */
    @PostMapping("/{id}/register")
    public ResponseEntity<EventResponseDTO> registerParticipant(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String username = request.get("username");
        if (username == null || username.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EventResponseDTO updatedEvent = eventService.registerParticipant(id, username);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }
}
