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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.neu.nuboard.utils.SnowflakeIDGenerator;

/**
 * Service class for managing event registration-related business logic.
 * Provides methods to register, unregister, and retrieve event registrations.
 */
@Service
public class EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SnowflakeIDGenerator snowflakeIDGenerator;

    /**
     * Constructs an EventRegistrationService with the specified repositories and ID generator.
     *
     * @param registrationRepository the repository for event registration persistence operations must not be null
     * @param eventRepository the repository for event persistence operations must not be null
     * @param userRepository the repository for user persistence operations must not be null
     * @param snowflakeIDGenerator the generator for unique Long IDs must not be null
     */
    @Autowired
    public EventRegistrationService(
            @NonNull EventRegistrationRepository registrationRepository,
            @NonNull EventRepository eventRepository,
            @NonNull UserRepository userRepository,
            @NonNull SnowflakeIDGenerator snowflakeIDGenerator) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.snowflakeIDGenerator = snowflakeIDGenerator;
    }

    /**
     * Registers a user for an event.
     *
     * @param eventId the ID of the event must not be null or empty
     * @param userId the ID of the user to register, must not be null
     * @return the saved EventRegistration entity
     * @throws BusinessException if the event or user is not found, the input is invalid, or the user is already registered
     */
    public EventRegistration registerForEvent(@NonNull Long eventId, @NonNull Long userId) {
        // Find the event and user
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Check for duplicate registration
        if (registrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED);
        }

        // Create and save the registration with Snowflake ID
        EventRegistration registration = new EventRegistration(event, user);
        registration.setId(snowflakeIDGenerator.nextId());
        try {
            registrationRepository.save(registration);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED);
        }

        return registration;
    }

    /**
     * Unregisters a user from an event.
     *
     * @param eventId the ID of the event must not be null or empty
     * @param userId the ID of the user to unregister must not be null
     * @return the deleted EventRegistration entity (if found)
     * @throws BusinessException if the input is invalid or the registration is not found
     */
    public EventRegistration unregisterForEvent(@NonNull Long eventId, @NonNull Long userId) {
        // Find the registration
        EventRegistration registration = registrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REGISTRATION_NOT_FOUND));

        // Delete the registration
        registrationRepository.delete(registration);

        return registration;
    }

    /**
     * Retrieves all registrations for a specific event.
     *
     * @param eventId the ID of the event
     * @return a list of {@link EventRegistrationDTO} containing the registration details
     * @throws BusinessException if the event ID is invalid
     */
    public List<EventRegistrationDTO> getRegistrationsByEventId(@NonNull Long eventId) {
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
    public List<EventRegistrationDTO> getRegistrationsByUserId(@NonNull Long userId) {
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
    private EventRegistrationDTO mapToDTO(@Nullable EventRegistration registration) {
        if (registration == null) {
            return null;
        }
        return new EventRegistrationDTO(
                registration.getId(),
                registration.getEvent() != null ? registration.getEvent().getId() : null,
                registration.getUser() != null ? registration.getUser().getId() : null
        );
    }
}