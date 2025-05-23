package com.neu.nuboard.repository;

import com.neu.nuboard.model.Event;
import com.neu.nuboard.model.OrganizerType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Event entity.
 * Extends JpaRepository to provide CRUD operations for the event table.
 * - save() method is used to save a new event to the database.
 * - findAll() method is used to retrieve all events from the database.
 * - deleteById() method is used to delete an event by its ID.
 * - findById() method is used to retrieve an event by its ID.
 */
public interface EventRepository extends JpaRepository<Event, String> {

    /**
     * Find all events created by a specific user.
     *
     * @param creatorId The ID of the creator.
     * @return A list of events created by the specified user.
     */
    List<Event> findByCreatorId(String creatorId);

    /**
     * Find all events of a specific organizer type.
     *
     * @param organizerType The type of organizer (e.g., SCHOOL or CORPORATE).
     * @return A list of events with the specified organizer type.
     */
    List<Event> findByOrganizerType(OrganizerType organizerType);

    /**
     * Find all events that start within a given time range.
     *
     * @param start The start of the time range.
     * @param end The end of the time range.
     * @return A list of events that start between the specified times.
     */
    List<Event> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
