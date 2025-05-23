package com.neu.nuboard.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for returning event details in API responses.
 */
public class EventResponseDTO {

    private String id;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private String creatorId; // 返回創建者的 ID
    private Set<String> participants; // 返回參加者的 ID 集合

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
     * Get the date of the event.
     * @return The date of the event.
     */
    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    /**
     * Get the location of the event.
     * @return The location of the event.
     */
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

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
    public void setParticipants(Set<String> participants) { this.participants = participants; }
    public void setCreatorId(UUID id2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCreatorId'");
    }
}
