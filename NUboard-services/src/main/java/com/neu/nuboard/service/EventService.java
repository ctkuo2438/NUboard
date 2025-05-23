package com.neu.nuboard.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.model.Event;
import com.neu.nuboard.model.User;
import com.neu.nuboard.repository.EventRepository;
import com.neu.nuboard.repository.UserRepository;

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
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
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

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(String id) {
        return eventRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));
    }

    @Transactional
    public Event registerUserForEvent(String eventId, String userId) {
        Event event = getEventById(eventId);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (event.getParticipants().contains(user)) {
            throw new BusinessException(ErrorCode.USER_ALREADY_REGISTERED);
        }

        event.getParticipants().add(user);
        return eventRepository.save(event);
    }

    public Set<User> getEventParticipants(String eventId) {
        Event event = getEventById(eventId);
        return event.getParticipants();
    }
} 