package com.neu.nuboard.dto;

public class UserStatusResponseDTO {
    private Long id;
    private String username;
    private Boolean enabled;

    // Constructors
    public UserStatusResponseDTO() {}

    public UserStatusResponseDTO(Long id, String username, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.enabled = enabled;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
