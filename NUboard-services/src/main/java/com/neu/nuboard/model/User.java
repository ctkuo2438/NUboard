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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "program", nullable = false, columnDefinition = "TEXT")
    private String program;

    @Column(name = "email", nullable = false)
    private String email;

    /**
     * The set of registrations for this user.
     * Each registration links this user to an event.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventRegistration> registrations = new HashSet<>();

    /**
     * Default no-arg constructor required by JPA.
     */
    protected User() {
        this.id = UUIDutil.getId();
    }

    /**
     * Constructor for a fully specified user.
     *
     * @param name The name of the user.
     * @param program The program of the user.
     * @param email The email of the user.
     */
    public User(String name, String program, String email) {
        this.id = UUIDutil.getId();
        this.setName(name);
        this.setProgram(program);
        this.setEmail(email);
        this.registrations = new HashSet<>();
    }


    // Getters and Setters
    /**
     * Get the ID of the user.
     * @return The ID of the user.
     */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    /**
     * Get the name of the user.
     * @return The name of the user.
     */
    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    /**
     * Get the program of the user.
     * @return The program of the user.
     */
    public String getProgram() { return program; }
    public void setProgram(String program) {
        if (program == null || program.trim().isEmpty()) {
            throw new IllegalArgumentException("Program cannot be null or empty.");
        }
        this.program = program;
    }

    /**
     * Get the email of the user.
     * @return The email of the user.
     */
    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        this.email = email;
    }

    /**
     * Get the set of registrations for this user.
     * @return The set of registrations.
     */
    public Set<EventRegistration> getRegistrations() { return registrations; }
    public void setRegistrations(Set<EventRegistration> registrations) {
        this.registrations = (registrations != null) ? registrations : new HashSet<>();
    }

    /**
     * Validates that required fields are present and not empty.
     * @return True if the user is valid, false otherwise.
     */
    public boolean validate() {
        return name != null && !name.trim().isEmpty() &&
                program != null && !program.trim().isEmpty() &&
                email != null && !email.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", program='" + program + '\'' +
                ", email='" + email + '\'' +
                ", registrations=" + registrations +
                '}';
    }
}