package com.neu.nuboard.dto;
import java.time.LocalDateTime;

/**
 * DTO(Data Transfer Object) for creating an event.
 * only required fields are provided from the client.
 * different from Event.java, EventCreateDTO is not a JPA entity.
 * */
public class EventCreateDTO {
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private String creatorId;

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
}
