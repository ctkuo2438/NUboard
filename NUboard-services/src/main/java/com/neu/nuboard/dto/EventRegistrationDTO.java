package com.neu.nuboard.dto;

public class EventRegistrationDTO {

    private Long id;
    private String eventId;
    private Long userId;

    /**
     * Default constructor.
     */
    public EventRegistrationDTO() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id The ID of the registration record.
     * @param eventId The ID of the event.
     * @param userId The ID of the user.
     */
    public EventRegistrationDTO(Long id, String eventId, Long userId) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
    }

    /**
     * Get the ID of the registration record.
     * @return The ID of the registration record.
     */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /**
     * Get the ID of the event.
     * @return The ID of the event.
     */
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    /**
     * Get the ID of the user.
     * @return The ID of the user.
     */
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
