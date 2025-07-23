package com.neu.nuboard.dto;

import java.util.List;

public class RoleResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<PermissionResponseDTO> permissions;
    private Long userCount;

    // Constructors
    public RoleResponseDTO() {}

    public RoleResponseDTO(Long id, String name, String description,
                           List<PermissionResponseDTO> permissions, Long userCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.userCount = userCount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<PermissionResponseDTO> getPermissions() { return permissions; }
    public void setPermissions(List<PermissionResponseDTO> permissions) { this.permissions = permissions; }

    public Long getUserCount() { return userCount; }
    public void setUserCount(Long userCount) { this.userCount = userCount; }
}
