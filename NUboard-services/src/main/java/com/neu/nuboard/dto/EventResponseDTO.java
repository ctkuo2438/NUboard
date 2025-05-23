package com.neu.nuboard.dto;

import com.neu.nuboard.model.OrganizerType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for returning event details in API responses.
 */
public class EventResponseDTO {

    private String id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private OrganizerType organizerType;
    private String creatorId;
    private Set<String> participants;

    /**
     * Default no-arg constructor required by JPA.
     */
    public EventResponseDTO() {
        this.participants = new HashSet<>();
    }

    /**
     * Constructor with all fields.
     *
     * @param id The ID of the event.
     * @param title The title of the event.
     * @param description The description of the event.
     * @param startTime The start time of the event.
     * @param endTime The end time of the event.
     * @param location The location of the event.
     * @param organizerType The type of organizer (school or corporate).
     * @param creatorId The ID of the creator.
     * @param participants The set of participant IDs.
     */
    public EventResponseDTO(String id, String title, String description, LocalDateTime startTime, LocalDateTime endTime,
                            String location, OrganizerType organizerType, String creatorId, Set<String> participants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.organizerType = organizerType;
        this.creatorId = creatorId;
        this.participants = (participants != null) ? participants : new HashSet<>();
    }

    /**
     * Get the ID of the event.
     * @return The ID of the event.
     */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    /**
     * Get the title of the event.
     * @return The title of the event.
     */
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    /**
     * Get the description of the event.
     * @return The description of the event.
     */
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    /**
     * Get the start time of the event.
     * @return The start time of the event.
     */
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    /**
     * Get the end time of the event.
     * @return The end time of the event.
     */
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    /**
     * Get the location of the event.
     * @return The location of the event.
     */
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    /**
     * Get the organizer type of the event.
     * @return The organizer type of the event.
     */
    public OrganizerType getOrganizerType() { return organizerType; }
    public void setOrganizerType(OrganizerType organizerType) { this.organizerType = organizerType; }

    /**
     * Get the ID of the creator.
     * @return The ID of the creator.
     */
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }

    /**
     * Get the set of participant IDs of the event.
     * @return The set of participant IDs.
     */
    public Set<String> getParticipants() { return participants; }
    public void setParticipants(Set<String> participants) {
        this.participants = (participants != null) ? participants : new HashSet<>();
    }
}