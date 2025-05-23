package com.neu.nuboard.service;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.model.Event;
import com.neu.nuboard.model.OrganizerType;
import com.neu.nuboard.model.User;
import com.neu.nuboard.repository.EventRepository;
import com.neu.nuboard.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing event-related business logic.
 * Provides methods to create, retrieve, and filter events in the system.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    /**
     * Constructs an EventService with the specified repositories.
     *
     * @param eventRepository the repository for event persistence operations
     * @param userRepository the repository for user persistence operations
     */
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new event based on the provided {@link EventCreateDTO}.
     *
     * @param eventCreateDTO the DTO containing event details
     * @return an {@link EventResponseDTO} containing the saved event details
     * @throws BusinessException if the input is invalid or the creator is not found
     */
    public EventResponseDTO createEvent(EventCreateDTO eventCreateDTO) {
        // Validate input
        if (eventCreateDTO.getTitle() == null || eventCreateDTO.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (eventCreateDTO.getStartTime() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (eventCreateDTO.getEndTime() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (eventCreateDTO.getLocation() == null || eventCreateDTO.getLocation().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (eventCreateDTO.getOrganizerType() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (eventCreateDTO.getCreatorId() == null || eventCreateDTO.getCreatorId().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        // Find the creator
        User creator = userRepository.findById(eventCreateDTO.getCreatorId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Map EventCreateDTO to Event entity
        Event event = new Event(
                eventCreateDTO.getTitle(),
                eventCreateDTO.getDescription(),
                eventCreateDTO.getStartTime(),
                eventCreateDTO.getEndTime(),
                eventCreateDTO.getLocation(),
                eventCreateDTO.getOrganizerType(),
                creator
        );

        // Save the event to the database
        Event savedEvent = eventRepository.save(event);

        // Map saved Event to EventResponseDTO
        return mapToResponseDTO(savedEvent);
    }

    /**
     * Retrieves all events from the database.
     *
     * @return a list of {@link EventResponseDTO} containing all event details
     */
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all events created by a specific user.
     *
     * @param creatorId the ID of the creator
     * @return a list of {@link EventResponseDTO} containing the events created by the specified user
     * @throws BusinessException if the creator ID is invalid
     */
    public List<EventResponseDTO> getEventsByCreatorId(String creatorId) {
        if (creatorId == null || creatorId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        return eventRepository.findByCreatorId(creatorId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all events of a specific organizer type.
     *
     * @param organizerType the type of organizer (e.g., SCHOOL or CORPORATE)
     * @return a list of {@link EventResponseDTO} containing the events of the specified organizer type
     * @throws BusinessException if the organizer type is null
     */
    public List<EventResponseDTO> getEventsByOrganizerType(OrganizerType organizerType) {
        if (organizerType == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        return eventRepository.findByOrganizerType(organizerType)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all events that start within a given time range.
     *
     * @param start the start of the time range
     * @param end the end of the time range
     * @return a list of {@link EventResponseDTO} containing the events that start between the specified times
     * @throws BusinessException if the start or end time is null, or if the start time is after the end time
     */
    public List<EventResponseDTO> getEventsByStartTimeBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (start.isAfter(end)) {
            throw new BusinessException(ErrorCode.INVALID_TIME_RANGE);
        }
        return eventRepository.findByStartTimeBetween(start, end)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the ID of the event
     * @return an {@link EventResponseDTO} containing the event details
     * @throws BusinessException if the event ID is invalid or the event is not found
     */
    public EventResponseDTO getEventById(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));
        return mapToResponseDTO(event);
    }

    /**
     * Maps an {@link Event} entity to an {@link EventResponseDTO}.
     *
     * @param event the Event entity to map
     * @return an {@link EventResponseDTO} containing the event details
     */
    private EventResponseDTO mapToResponseDTO(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getLocation(),
                event.getOrganizerType(),
                event.getCreator().getId(),
                event.getRegistrations().stream()
                        .map(registration -> registration.getUser().getId())
                        .collect(Collectors.toSet())
        );
    }
}