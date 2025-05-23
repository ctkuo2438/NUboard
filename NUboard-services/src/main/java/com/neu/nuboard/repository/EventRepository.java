package com.neu.nuboard.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neu.nuboard.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
} 