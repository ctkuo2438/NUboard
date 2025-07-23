package com.neu.nuboard.dto;

import java.util.List;
import java.util.Set;

public class UserRoleResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String program;
    private Boolean enabled;
    private LocationResponseDTO location;  // Using your existing DTO
    private CollegeResponseDTO college;    // Using your existing DTO
    private String authProvider;
    private List<String> roles;
    private Set<String> permissions;

    // Default constructor
    public UserRoleResponseDTO() {}

    // All-args constructor
    public UserRoleResponseDTO(Long id, String username, String email, String program,
                               Boolean enabled, LocationResponseDTO location, CollegeResponseDTO college,
                               String authProvider, List<String> roles, Set<String> permissions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.program = program;
        this.enabled = enabled;
        this.location = location;
        this.college = college;
        this.authProvider = authProvider;
        this.roles = roles;
        this.permissions = permissions;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public LocationResponseDTO getLocation() { return location; }
    public void setLocation(LocationResponseDTO location) { this.location = location; }

    public CollegeResponseDTO getCollege() { return college; }
    public void setCollege(CollegeResponseDTO college) { this.college = college; }

    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
}