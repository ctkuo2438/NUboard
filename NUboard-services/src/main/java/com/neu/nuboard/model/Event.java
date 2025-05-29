package com.neu.nuboard.model;

import com.neu.nuboard.dto.EventCreateDTO;
import com.neu.nuboard.utils.UUIDutil;
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
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

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
        this.id = UUIDutil.getId();
    }

    /**
     * Creates a new Event instance from EventCreateDTO.
     * @param dto The DTO containing event details
     * @return A new Event instance
     */
    public static Event fromDTO(EventCreateDTO dto) {
        Event event = new Event();
        event.title = dto.getTitle();
        event.description = dto.getDescription();
        event.startTime = dto.getStartTime();
        event.endTime = dto.getEndTime();
        event.location = dto.getLocation();
        event.address = dto.getAddress();
        event.creatorId = dto.getCreatorId();
        event.organizerType = dto.getOrganizerType();
        return event;
    }

    /**
     * Updates the event with data from EventCreateDTO.
     * @param dto The DTO containing updated event details
     */
    public void updateFromDTO(EventCreateDTO dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.location = dto.getLocation();
        this.address = dto.getAddress();
        this.creatorId = dto.getCreatorId();
        this.organizerType = dto.getOrganizerType();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getLocation() { return location; }
    public String getAddress() { return address; }
    public String getCreatorId() { return creatorId; }
    public Set<String> getParticipants() { return participants; }
    public OrganizerType getOrganizerType() { return organizerType; }

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
