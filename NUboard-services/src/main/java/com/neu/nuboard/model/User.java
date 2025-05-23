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

    @Column(name = "username", nullable = false, unique = true)
    private String username;

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
     */
    public User(String username) {
        this.id = UUIDutil.getId();
        this.setUsername(username);
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
                '}';
    }
}