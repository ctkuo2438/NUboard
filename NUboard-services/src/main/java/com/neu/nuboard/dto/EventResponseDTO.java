package com.neu.nuboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neu.nuboard.model.Event.OrganizerType;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for returning event details in API responses.
 */
public class EventResponseDTO {

    private String id;
    private String title;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    private Long locationId;
    private String address;
    private String creatorId;
    private OrganizerType organizerType;
    private Set<EventRegistrationDTO> registrations;

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
     * Get the location ID of the event.
     * @return The location ID of the event.
     */
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

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

    /**
     * Get the registrations for this event.
     * @return The set of registrations.
     */
    public Set<EventRegistrationDTO> getRegistrations() { return registrations; }
    public void setRegistrations(Set<EventRegistrationDTO> registrations) { this.registrations = registrations; }
}
