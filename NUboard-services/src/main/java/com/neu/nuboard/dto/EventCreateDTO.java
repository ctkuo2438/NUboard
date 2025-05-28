package com.neu.nuboard.dto;

import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
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
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty() || title.length() > 255) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TITLE);
        }
        this.title = title;
    }

    /**
     * Get the description of the event.
     * @return The description of the event.
     */
    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description != null && description.length() > 1024) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        this.description = description;
    }

    /**
     * Get the start time of the event.
     * @return The start time of the event.
     */
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
        this.startTime = startTime;
    }

    /**
     * Get the end time of the event.
     * @return The end time of the event.
     */
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) {
        if (endTime == null) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
        if (startTime != null && endTime.isBefore(startTime)) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
        this.endTime = endTime;
    }

    /**
     * Get the location of the event.
     * @return The location of the event.
     */
    public String getLocation() { return location; }
    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty() || location.length() > 255) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_LOCATION);
        }
        this.location = location;
    }

    /**
     * Get the address of the event.
     * @return The address of the event.
     */
    public String getAddress() { return address; }
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty() || address.length() > 255) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_ADDRESS);
        }
        this.address = address;
    }

    /**
     * Get the creator of the event.
     * @return The name of the creator.
     */
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) {
        if (creatorId == null || creatorId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_CREATOR);
        }
        this.creatorId = creatorId;
    }

    /**
     * Get the organizer type of the event.
     * @return The organizer type of the event.
     */
    public OrganizerType getOrganizerType() { return organizerType; }
    public void setOrganizerType(OrganizerType organizerType) {
        if (organizerType == null) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_ORGANIZER);
        }
        this.organizerType = organizerType;
    }
}
