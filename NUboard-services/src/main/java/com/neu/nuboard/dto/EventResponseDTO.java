package com.neu.nuboard.dto;

import com.neu.nuboard.model.Event.OrganizerType;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for returning event details in API responses.
 */
public class EventResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String address;
    private String creatorId;
    private Set<String> participants;
    private OrganizerType organizerType;

    /**
     * Get the ID of the event.
     * @return The ID of the event.
     */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
     * Get the address of the event.
     * @return The address of the event.
     */
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    /**
     * Get the creator of the event.
     * @return The name of the creator.
     */
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }

    /**
     * Get the participants of the event.
     * @return The set of participant usernames.
     */
    public Set<String> getParticipants() { return participants; }
    public void setParticipants(Set<String> participants) { this.participants = participants; }

    /**
     * Get the organizer type of the event.
     * @return The organizer type of the event.
     */
    public OrganizerType getOrganizerType() { return organizerType; }
    public void setOrganizerType(OrganizerType organizerType) { this.organizerType = organizerType; }
}
