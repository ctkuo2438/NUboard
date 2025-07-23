package com.neu.nuboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.model.Event.OrganizerType;
import com.neu.nuboard.model.Location;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * DTO(Data Transfer Object) for creating an event.
 * only required fields are provided from the client.
 * different from Event.java, EventCreateDTO is not a JPA entity.
 * */
public class EventCreateDTO {
    private String title;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String endTime;
    private Long locationId;
    private String address;
    private Long creatorId;
    private String organizerType;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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
    public LocalDateTime getStartTime() {
        try {
            return LocalDateTime.parse(startTime, formatter);
        } catch (DateTimeParseException e) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
    }
    
    public void setStartTime(String startTime) {
        if (startTime == null || startTime.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
        this.startTime = startTime;
    }

    /**
     * Get the end time of the event.
     * @return The end time of the event.
     */
    public LocalDateTime getEndTime() {
        try {
            return LocalDateTime.parse(endTime, formatter);
        } catch (DateTimeParseException e) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
    }
    
    public void setEndTime(String endTime) {
        if (endTime == null || endTime.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
        try {
            LocalDateTime end = LocalDateTime.parse(endTime, formatter);
            if (startTime != null) {
                LocalDateTime start = LocalDateTime.parse(startTime, formatter);
                if (end.isBefore(start)) {
                    throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
                }
            }
        } catch (DateTimeParseException e) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_TIME);
        }
        this.endTime = endTime;
    }

    /**
     * Get the location ID of the event.
     * @return The location ID of the event.
     */
    public Long getLocationId() { 
        return locationId;
    }
    
    public void setLocationId(Long locationId) {
        if (locationId == null) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_LOCATION);
        }
        this.locationId = locationId;
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
    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) {
        if (creatorId == null) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_CREATOR);
        }
        this.creatorId = creatorId;
    }

    /**
     * Get the organizer type of the event.
     * @return The organizer type of the event.
     */
    public OrganizerType getOrganizerType() {
        try {
            return OrganizerType.valueOf(organizerType);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_ORGANIZER);
        }
    }
    
    public void setOrganizerType(String organizerType) {
        if (organizerType == null || organizerType.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_ORGANIZER);
        }
        try {
            OrganizerType.valueOf(organizerType);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_ORGANIZER);
        }
        this.organizerType = organizerType;
    }
}
