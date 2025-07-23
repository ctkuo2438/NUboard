package com.neu.nuboard.dto;

import java.time.LocalDateTime;

public class RoleActivityResponseDTO {
    private String user;
    private String role;
    private LocalDateTime assignedAt;
    private String assignedBy;

    // Constructors
    public RoleActivityResponseDTO() {}

    public RoleActivityResponseDTO(String user, String role, LocalDateTime assignedAt, String assignedBy) {
        this.user = user;
        this.role = role;
        this.assignedAt = assignedAt;
        this.assignedBy = assignedBy;
    }

    // Getters and Setters
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }
}
