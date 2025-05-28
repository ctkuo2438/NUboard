package com.neu.nuboard.repository;

import com.neu.nuboard.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Convention over Configuration design pattern
 * CRUD operations for the event table.
 * save() method is used to save a new event to the database.
 * findAll() method is used to retrieve all the events from the database.
 * deleteById() method is used to delete an event by its id.
 * findById() method is used to retrieve an event by its id.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    /**
     * Search events by keyword in title or description.
     * @param keyword The search keyword.
     * @return List of events matching the search criteria.
     */
    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Event> searchEvents(@Param("keyword") String keyword);

    /**
     * Find events by organizer type.
     * @param organizerType The type of organizer.
     * @return List of events with the specified organizer type.
     */
    @Query("SELECT e FROM Event e WHERE e.organizerType = :organizerType")
    List<Event> findByOrganizerType(@Param("organizerType") Event.OrganizerType organizerType);
}
