package com.neu.nuboard.service;

import com.neu.nuboard.dto.EventRegistrationDTO;
import com.neu.nuboard.model.Event;
import com.neu.nuboard.model.EventRegistration;
import com.neu.nuboard.model.User;
import com.neu.nuboard.repository.EventRegistrationRepository;
import com.neu.nuboard.repository.EventRepository;
import com.neu.nuboard.repository.UserRepository;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing event registration-related business logic.
 * Provides methods to register, unregister, and retrieve event registrations.
 */
@Service
public class EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    /**
     * Constructs an EventRegistrationService with the specified repositories.
     *
     * @param registrationRepository the repository for event registration persistence operations
     * @param eventRepository the repository for event persistence operations
     * @param userRepository the repository for user persistence operations
     */
    public EventRegistrationService(EventRegistrationRepository registrationRepository,
                                    EventRepository eventRepository,
                                    UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /**
     * Registers a user for an event.
     *
     * @param eventId the ID of the event
     * @param userId the ID of the user to register
     * @throws BusinessException if the event or user is not found, the input is invalid, or the user is already registered
     */
    public void registerForEvent(String eventId, String userId) {
        // Validate input
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        // Find the event and user
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Check for duplicate registration
        if (registrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED);
        }

        // Create and save the registration
        EventRegistration registration = new EventRegistration(event, user);
        try {
            registrationRepository.save(registration);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED);
        }
    }

    /**
     * Unregisters a user from an event.
     *
     * @param eventId the ID of the event
     * @param userId the ID of the user to unregister
     * @throws BusinessException if the input is invalid or the registration is not found
     */
    public void unregisterForEvent(String eventId, String userId) {
        // Validate input
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }

        // Find the registration
        EventRegistration registration = registrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REGISTRATION_NOT_FOUND));

        // Delete the registration
        registrationRepository.delete(registration);
    }

    /**
     * Retrieves all registrations for a specific event.
     *
     * @param eventId the ID of the event
     * @return a list of {@link EventRegistrationDTO} containing the registration details
     * @throws BusinessException if the event ID is invalid
     */
    public List<EventRegistrationDTO> getRegistrationsByEventId(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }
        return registrationRepository.findByEventId(eventId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all registrations for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link EventRegistrationDTO} containing the registration details
     * @throws BusinessException if the user ID is invalid
     */
    public List<EventRegistrationDTO> getRegistrationsByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }
        return registrationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all registrations.
     *
     * @return a list of {@link EventRegistrationDTO} containing all registration details
     */
    public List<EventRegistrationDTO> getAllRegistrations() {
        return registrationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps an {@link EventRegistration} entity to an {@link EventRegistrationDTO}.
     *
     * @param registration the EventRegistration entity to map
     * @return an {@link EventRegistrationDTO} containing the registration details
     */
    private EventRegistrationDTO mapToDTO(EventRegistration registration) {
        return new EventRegistrationDTO(
                registration.getId(),
                registration.getEvent().getId(),
                registration.getUser().getId().toString()
        );
    }
}