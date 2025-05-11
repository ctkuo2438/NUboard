package com.neu.nuboard.service;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.exception.*;
import com.neu.nuboard.model.Event;
import com.neu.nuboard.model.User;
import com.neu.nuboard.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing event-related business logic.
 */
@Service
public class EventService {
    // eventRepository is an object that implements the EventRepository interface for CRUD operations on the event table.
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Creates a new event based on the provided EventCreateDTO.
     * @param eventCreateDTO The DTO containing event details.
     * @return EventResponseDTO containing the saved event details.
     */
    public EventResponseDTO createEvent(EventCreateDTO eventCreateDTO) {
        // Validate input
        if (eventCreateDTO.getTitle() == null || eventCreateDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be null or empty");
        }
        if (eventCreateDTO.getEventDate() == null) {
            throw new IllegalArgumentException("Event date cannot be null");
        }
        if (eventCreateDTO.getLocation() == null || eventCreateDTO.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Event location cannot be null or empty");
        }
        if (eventCreateDTO.getCreatorId() == null || eventCreateDTO.getCreatorId().trim().isEmpty()) {
            throw new IllegalArgumentException("Creator ID cannot be null or empty");
        }

        // Map EventCreateDTO to Event entity
        User creator = new User("temp-user"); // temporary username
        creator.setId(eventCreateDTO.getCreatorId());
        Event event = new Event(
                eventCreateDTO.getTitle(),
                eventCreateDTO.getDescription(),
                eventCreateDTO.getEventDate(),
                eventCreateDTO.getLocation(),
                creator,
                new HashSet<>()
        );

        // Save the event to the database
        Event savedEvent = eventRepository.save(event);

        // Map saved Event to EventResponseDTO
        return mapToResponseDTO(savedEvent);
    }

    /**
     * Retrieves all events from the database.
     * @return List of EventResponseDTO containing all event details.
     */
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Registers a user for an event.
     * @param eventId The ID of the event.
     * @param userId The ID of the user to register.
     */
    public void registerForEvent(String eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        // Create a User object for the participant
        User participant = new User("temp-user"); // temporary username
        participant.setId(userId);

        // Add participant to the event
        event.getParticipants().add(participant);
        eventRepository.save(event);
    }

    /**
     * Maps an Event entity to an EventResponseDTO.
     * @param event The Event entity to map.
     * @return EventResponseDTO containing the event details.
     */
    private EventResponseDTO mapToResponseDTO(Event event) {
        EventResponseDTO responseDTO = new EventResponseDTO();
        responseDTO.setId(event.getId());
        responseDTO.setTitle(event.getTitle());
        responseDTO.setDescription(event.getDescription());
        responseDTO.setEventDate(event.getEventDate());
        responseDTO.setLocation(event.getLocation());
        responseDTO.setCreatorId(event.getCreator().getId());
        responseDTO.setParticipants(
                event.getParticipants().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );
        return responseDTO;
    }
}