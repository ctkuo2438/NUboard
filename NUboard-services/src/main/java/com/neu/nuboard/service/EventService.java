package com.neu.nuboard.service;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.dto.EventRegistrationDTO;
import com.neu.nuboard.exception.*;
import com.neu.nuboard.model.Event;
import com.neu.nuboard.model.Location;
import com.neu.nuboard.repository.EventRepository;
import com.neu.nuboard.repository.LocationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

/**
 * Service class for managing event-related business logic.
 */
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Creates a new event based on the provided EventCreateDTO.
     * @param eventCreateDTO The DTO containing event details.
     * @return EventResponseDTO containing the saved event details.
     */
    public EventResponseDTO createEvent(EventCreateDTO eventCreateDTO) {
        // Find the location by ID
        Location location = locationRepository.findById(eventCreateDTO.getLocationId())
            .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_INVALID_LOCATION));

        // Create event using factory method
        Event event = Event.fromDTO(eventCreateDTO);
        event.setLocation(location);

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

        // Find the location by ID
        Location location = locationRepository.findById(eventCreateDTO.getLocationId())
            .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_INVALID_LOCATION));

        // Update event using the update method
        existingEvent.updateFromDTO(eventCreateDTO);
        existingEvent.setLocation(location);

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

        // Delete the event directly
        eventRepository.deleteById(id);
    }

    /**
     * Retrieves all events created by a specific user.
     * @param creatorId The ID of the creator.
     * @return List of EventResponseDTO containing events created by the user.
     */
    public List<EventResponseDTO> getEventsByCreatorId(String creatorId) {
        return eventRepository.findAll()
                .stream()
                .filter(event -> event.getCreatorId().equals(creatorId))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves events by location.
     * @param locationId The ID of the location.
     * @return List of EventResponseDTO containing events at the specified location.
     */
    public List<EventResponseDTO> getEventsByLocation(Long locationId) {
        return eventRepository.findAll()
                .stream()
                .filter(event -> event.getLocationId() != null && event.getLocationId().equals(locationId))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves events by organizer type.
     * @param organizerType The type of organizer.
     * @return List of EventResponseDTO containing events with the specified organizer type.
     */
    public List<EventResponseDTO> getEventsByOrganizerType(Event.OrganizerType organizerType) {
        return eventRepository.findByOrganizerType(organizerType)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
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
        responseDTO.setLocationId(event.getLocationId());
        responseDTO.setAddress(event.getAddress());
        responseDTO.setCreatorId(event.getCreatorId());
        responseDTO.setOrganizerType(event.getOrganizerType());

        // Map registrations to DTOs
        Set<EventRegistrationDTO> registrationDTOs = event.getRegistrations().stream()
            .map(registration -> new EventRegistrationDTO(
                registration.getId(),
                registration.getEvent().getId(),
                registration.getUser().getId()
            ))
            .collect(Collectors.toSet());
        responseDTO.setRegistrations(registrationDTOs);

        return responseDTO;
    }
}