package com.neu.nuboard.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.utils.UUIDutil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "location")
    private String location;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @ManyToMany
    private Set<User> participants = new HashSet<>();

    protected Event() {
        this.id = UUIDutil.getId();
    }

    public Event(String title, String description, LocalDateTime eventDate, String location, UUID creatorId) {
        this.id = UUIDutil.getId();
        this.setTitle(title);
        this.setDescription(description);
        this.setEventDate(eventDate);
        this.setLocation(location);
        this.setCreatorId(creatorId);
    }

    public UUID getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_TITLE);
        }
        if (title.length() > 255) {
            throw new BusinessException(ErrorCode.OVER_MAX_LENGTH);
        }
        this.title = title;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description != null && description.length() > 1000) {
            throw new BusinessException(ErrorCode.OVER_MAX_LENGTH);
        }
        this.description = description;
    }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) {
        if (eventDate == null) {
            throw new BusinessException(ErrorCode.EMPTY_EVENT_DATE);
        }
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.INVALID_EVENT_DATE);
        }
        this.eventDate = eventDate;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        if (location != null && location.length() > 255) {
            throw new BusinessException(ErrorCode.OVER_MAX_LENGTH);
        }
        this.location = location;
    }

    public UUID getCreatorId() { return creatorId; }
    public void setCreatorId(UUID creatorId) {
        if (creatorId == null) {
            throw new BusinessException(ErrorCode.EMPTY_CREATOR_ID);
        }
        this.creatorId = creatorId;
    }

    public Set<User> getParticipants() { return participants; }
    public void setParticipants(Set<User> participants) { this.participants = participants; }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", location='" + location + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", participants.size=" + (participants != null ? participants.size() : "0") +
                '}';
    }
} 