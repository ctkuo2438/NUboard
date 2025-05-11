package com.neu.nuboard.model;

import com.neu.nuboard.utils.UUIDutil;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @ManyToMany(mappedBy = "participants")
    private Set<Event> events = new HashSet<>();

    /**
     * Default no-arg constructor required by JPA.
     */
    protected User() {
        this.id = UUIDutil.getId();
    }

    /**
     * Constructor for a fully specified user.
     *
     * @param username The username of the user.
     * @param events The set of events the user is participating in.
     */
    public User(String username, Set<Event> events) {
        this.id = UUIDutil.getId();
        this.setUsername(username);
        this.events = (events != null) ? events : new HashSet<>();
    }

    /**
     * Constructs a basic user with only required fields.
     *
     * @param username The username of the user.
     */
    public User(String username) {
        this(username, new HashSet<>());
    }

    // Getters and Setters
    /**
     * Get the ID of the user.
     * @return The ID of the user.
     */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    /**
     * Get the username of the user.
     * @return The username of the user.
     */
    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        this.username = username;
    }

    /**
     * Get the set of events the user is participating in.
     * @return The set of events.
     */
    public Set<Event> getEvents() { return events; }
    public void setEvents(Set<Event> events) {
        this.events = (events != null) ? events : new HashSet<>();
    }

    /**
     * Validates that required fields are present and not empty.
     * @return True if the user is valid, false otherwise.
     */
    public boolean validate() {
        return username != null && !username.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", events=" + events +
                '}';
    }
}