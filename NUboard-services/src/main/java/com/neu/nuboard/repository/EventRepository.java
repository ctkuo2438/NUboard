package com.neu.nuboard.repository;

import com.neu.nuboard.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Convention over Configuration design pattern
 * CRUD operations for the event table.
 * save() method is used to save a new event to the database.
 * findAll() method is used to retrieve all the events from the database.
 * deleteById() method is used to delete an event by its id.
 * findById() method is used to retrieve an event by its id.
 */
public interface EventRepository extends JpaRepository<Event, String> {
}
