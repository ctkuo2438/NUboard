package com.neu.nuboard.repository;

import com.neu.nuboard.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EventRegistration entity.
 * Extends JpaRepository to provide CRUD operations for the event_registration table.
 * - save() method is used to save a new registration to the database.
 * - findAll() method is used to retrieve all registrations from the database.
 * - deleteById() method is used to delete a registration by its ID.
 * - findById() method is used to retrieve a registration by its ID.
 */
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    /**
     * Find all registrations for a specific event.
     *
     * @param eventId The ID of the event.
     * @return A list of registrations for the specified event.
     */
    List<EventRegistration> findByEventId(String eventId);

    /**
     * Find all registrations for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of registrations for the specified user.
     */
    List<EventRegistration> findByUserId(String userId);

    /**
     * Check if a registration exists for a specific event and user.
     *
     * @param eventId The ID of the event.
     * @param userId The ID of the user.
     * @return True if the registration exists, false otherwise.
     */
    boolean existsByEventIdAndUserId(String eventId, String userId);

    /**
     * Find a registration by event ID and user ID.
     *
     * @param eventId The ID of the event.
     * @param userId The ID of the user.
     * @return An Optional containing the registration if found, or empty if not found.
     */
    Optional<EventRegistration> findByEventIdAndUserId(String eventId, String userId);
}
