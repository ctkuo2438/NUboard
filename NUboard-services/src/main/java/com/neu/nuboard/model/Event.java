package com.neu.nuboard.model;

import com.neu.nuboard.dto.EventCreateDTO;
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
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = true, length = 1024)
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location", nullable = false, columnDefinition = "bigint")
    private Location location;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "organizer_type", nullable = false)
    private OrganizerType organizerType;

    /**
     * The set of registrations for this event.
     * Each registration links a user to this event.
     */
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventRegistration> registrations = new HashSet<>();

    public Event() {
        // Default constructor required by JPA
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
        this.address = dto.getAddress();
        this.creatorId = dto.getCreatorId();
        this.organizerType = dto.getOrganizerType();
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Location getLocation() { return location; }
    public Long getLocationId() { return location != null ? location.getId() : null; }
    public String getAddress() { return address; }
    public Long getCreatorId() { return creatorId; }
    public OrganizerType getOrganizerType() { return organizerType; }
    public Set<EventRegistration> getRegistrations() { return registrations; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setLocation(Location location) { this.location = location; }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", locationId=" + (location != null ? location.getId() : "null") +
                ", address='" + address + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", registrations=" + registrations +
                ", organizerType=" + organizerType +
                '}';
    }
}
