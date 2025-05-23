package com.neu.nuboard.service;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.exception.*;
import com.neu.nuboard.model.Event;
import com.neu.nuboard.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing event-related business logic.
 */
@Service
public class EventService {
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
            throw new BusinessException(ErrorCode.EVENT_INVALID_TITLE);
        }
        if (eventCreateDTO.getStartTime() == null) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
        if (eventCreateDTO.getEndTime() == null) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
        if (eventCreateDTO.getLocation() == null || eventCreateDTO.getLocation().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_LOCATION);
        }
        if (eventCreateDTO.getAddress() == null || eventCreateDTO.getAddress().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_ADDRESS);
        }
        if (eventCreateDTO.getCreatorId() == null || eventCreateDTO.getCreatorId().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_CREATOR);
        }
        if (eventCreateDTO.getOrganizerType() == null) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_ORGANIZER);
        }

        // Validate event time
        if (eventCreateDTO.getEndTime().isBefore(eventCreateDTO.getStartTime())) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }

        // Create event
        Event event = new Event();
        event.setTitle(eventCreateDTO.getTitle());
        event.setDescription(eventCreateDTO.getDescription());
        event.setStartTime(eventCreateDTO.getStartTime());
        event.setEndTime(eventCreateDTO.getEndTime());
        event.setLocation(eventCreateDTO.getLocation());
        event.setAddress(eventCreateDTO.getAddress());
        event.setCreatorId(eventCreateDTO.getCreatorId());
        event.setOrganizerType(eventCreateDTO.getOrganizerType());

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
     * Searches events by keyword in title or description.
     * @param keyword The search keyword.
     * @return List of EventResponseDTO containing matching events.
     */
    public List<EventResponseDTO> searchEvents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TITLE);
        }
        return eventRepository.searchEvents(keyword.trim())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing event.
     * @param id The ID of the event to update.
     * @param eventCreateDTO The DTO containing updated event details.
     * @return EventResponseDTO containing the updated event details.
     */
    public EventResponseDTO updateEvent(Long id, EventCreateDTO eventCreateDTO) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        // Validate event time
        if (eventCreateDTO.getEndTime().isBefore(eventCreateDTO.getStartTime())) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }

        // Update event fields
        existingEvent.setTitle(eventCreateDTO.getTitle());
        existingEvent.setDescription(eventCreateDTO.getDescription());
        existingEvent.setStartTime(eventCreateDTO.getStartTime());
        existingEvent.setEndTime(eventCreateDTO.getEndTime());
        existingEvent.setLocation(eventCreateDTO.getLocation());
        existingEvent.setAddress(eventCreateDTO.getAddress());
        existingEvent.setCreatorId(eventCreateDTO.getCreatorId());
        existingEvent.setOrganizerType(eventCreateDTO.getOrganizerType());

        Event updatedEvent = eventRepository.save(existingEvent);
        return mapToResponseDTO(updatedEvent);
    }

    /**
     * Deletes an event.
     * @param id The ID of the event to delete.
     */
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        // Clear participants before deleting the event
        event.getParticipants().clear();
        eventRepository.save(event);

        // Now delete the event
        eventRepository.deleteById(id);
    }

    /**
     * Registers a participant for an event.
     * @param eventId The ID of the event.
     * @param username The username of the participant.
     * @return EventResponseDTO containing the updated event details.
     */
    public EventResponseDTO registerParticipant(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_PARTICIPANT_INVALID);
        }

        // Check if user is already registered
        if (event.getParticipants().contains(username)) {
            throw new BusinessException(ErrorCode.EVENT_PARTICIPANT_ALREADY_REGISTERED);
        }

        event.getParticipants().add(username);
        Event updatedEvent = eventRepository.save(event);
        return mapToResponseDTO(updatedEvent);
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
        responseDTO.setStartTime(event.getStartTime());
        responseDTO.setEndTime(event.getEndTime());
        responseDTO.setLocation(event.getLocation());
        responseDTO.setAddress(event.getAddress());
        responseDTO.setCreatorId(event.getCreatorId());
        responseDTO.setParticipants(event.getParticipants());
        responseDTO.setOrganizerType(event.getOrganizerType());
        return responseDTO;
    }
}