package com.neu.nuboard.service;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.exception.*;
import com.neu.nuboard.model.Event;
import com.neu.nuboard.repository.EventRepository;
import org.springframework.stereotype.Service;
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
        // Create event using factory method
        Event event = Event.fromDTO(eventCreateDTO);

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
    public EventResponseDTO updateEvent(String id, EventCreateDTO eventCreateDTO) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        // Update event using the update method
        existingEvent.updateFromDTO(eventCreateDTO);

        Event updatedEvent = eventRepository.save(existingEvent);
        return mapToResponseDTO(updatedEvent);
    }

    /**
     * Deletes an event.
     * @param id The ID of the event to delete.
     */
    public void deleteEvent(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        // Clear participants before deleting the event
        event.getParticipants().clear();
        eventRepository.save(event);

        // Now delete the event
        eventRepository.deleteById(id);
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