package com.neu.nuboard.controller;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling event-related API requests.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    /**
     * Constructor for dependency injection.
     * @param eventService The EventService to handle business logic.
     */
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates a new event.
     * @param eventCreateDTO The DTO containing event details.
     * @return ResponseEntity containing the created event details.
     */
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO responseDTO = eventService.createEvent(eventCreateDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves all events.
     * @return ResponseEntity containing a list of all event details.
     */
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Registers a user for an event.
     * @param id The ID of the event.
     * @param userId The ID of the user to register.
     * @return ResponseEntity indicating success.
     */
    @PostMapping("/{id}/register")
    public ResponseEntity<Void> registerForEvent(@PathVariable String id, @RequestBody String userId) {
        eventService.registerForEvent(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
