package com.neu.nuboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "event")
public class Event {
    public enum OrganizerType {
        SCHOOL, CORPORATE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = true, length = 1024)
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "creator_id", nullable = false)
    private String creatorId;

    @ElementCollection
    @CollectionTable(name = "event_participants", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "username")
    private Set<String> participants = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "organizer_type", nullable = false)
    private OrganizerType organizerType;

    public Event() {
        // Default constructor required by JPA
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty() || title.length() > 255) {
            throw new IllegalArgumentException("Title cannot be null, empty, or longer than 255 characters");
        }
        this.title = title;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description != null && description.length() > 1024) {
            throw new IllegalArgumentException("Description cannot be longer than 1024 characters");
        }
        this.description = description;
    }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
        if (startTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        this.endTime = endTime;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty() || location.length() > 255) {
            throw new IllegalArgumentException("Location cannot be null, empty, or longer than 255 characters");
        }
        this.location = location;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty() || address.length() > 255) {
            throw new IllegalArgumentException("Address cannot be null, empty, or longer than 255 characters");
        }
        this.address = address;
    }

    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) {
        if (creatorId == null || creatorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Creator ID cannot be null or empty");
        }
        this.creatorId = creatorId;
    }

    public Set<String> getParticipants() { return participants; }
    public void setParticipants(Set<String> participants) {
        this.participants = (participants != null) ? participants : new HashSet<>();
    }

    public OrganizerType getOrganizerType() { return organizerType; }
    public void setOrganizerType(OrganizerType organizerType) {
        if (organizerType == null) {
            throw new IllegalArgumentException("Organizer type cannot be null");
        }
        this.organizerType = organizerType;
    }

    public boolean validate() {
        return title != null && !title.trim().isEmpty() && title.length() <= 255 &&
                (description == null || description.length() <= 1024) &&
                startTime != null && endTime != null && !endTime.isBefore(startTime) &&
                location != null && !location.trim().isEmpty() && location.length() <= 255 &&
                address != null && !address.trim().isEmpty() && address.length() <= 255 &&
                creatorId != null && !creatorId.trim().isEmpty() &&
                organizerType != null;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", participants=" + participants +
                ", organizerType=" + organizerType +
                '}';
    }
}
