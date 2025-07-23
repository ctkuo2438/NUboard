package com.neu.nuboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "event_registration",
        // The same user (user_id) cannot register for the same event (event_id) multiple times
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"}))
public class EventRegistration {

    @Id
    @Column(name = "id")
    // ID is manually generated using SnowflakeIDGenerator in the service layer
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // default constructor
    protected EventRegistration() {
    }

    // constructor
    public EventRegistration(Event event, User user) {
        this.event = event;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "EventRegistration{" +
                "id=" + id +
                ", event=" + (event != null ? event.getId() : null) +
                ", user=" + (user != null ? user.getId() : null) +
                '}';
    }
}
