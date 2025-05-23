package com.neu.nuboard.dto;

import com.neu.nuboard.model.Event.OrganizerType;
import java.time.LocalDateTime;

/**
 * DTO(Data Transfer Object) for creating an event.
 * only required fields are provided from the client.
 * different from Event.java, EventCreateDTO is not a JPA entity.
 * */
public class EventCreateDTO {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String address;
    private String creatorId;
    private OrganizerType organizerType;

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
     * Get the organizer type of the event.
     * @return The organizer type of the event.
     */
    public OrganizerType getOrganizerType() { return organizerType; }
    public void setOrganizerType(OrganizerType organizerType) { this.organizerType = organizerType; }
}
