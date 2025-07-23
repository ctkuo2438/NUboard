package com.neu.nuboard.dto;

import java.util.List;

public class PermissionWithRolesResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> roles;

    // Constructors
    public PermissionWithRolesResponseDTO() {}

    public PermissionWithRolesResponseDTO(Long id, String name, String description, List<String> roles) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
