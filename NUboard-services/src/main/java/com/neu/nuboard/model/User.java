package com.neu.nuboard.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @Column(name = "program", nullable = false)
    private String program;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventRegistration> registrations = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    // controls account status, enabled = true Account is ACTIVE, enabled = false Account is DISABLED
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "auth_provider")
    private String authProvider = "google";

    // ADDED: set a Profile completion flag for OAuth2 users
    @Column(name = "profile_completed", nullable = false)
    private boolean profileCompleted = false;

    /**
     * JPA要求的默认无参构造函数。
     */
    public User() {
    }

    /**
     * 创建一个完整参数的用户构造函数。
     *
     * @param username 用户名。
     * @param email 用户邮箱。
     */
    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.authProvider = "google";
        this.enabled = true;
        this.profileCompleted = false;
    }

    // Constructor for regular user creation (with program)
    public User(Long id, String username, String program, String email) {
        this.id = id;
        this.username = username;
        this.program = program;
        this.email = email;
        this.authProvider = "local";
        this.enabled = true;
        this.profileCompleted = false;
    }

    // Helper methods to work with roles
    public Set<Role> getRoles() {
        return userRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());
    }

    public void addRole(Role role) {
        UserRole userRole = new UserRole(this, role);
        userRoles.add(userRole);
    }

    public void removeRole(Role role) {
        userRoles.removeIf(ur -> ur.getRole().equals(role));
    }

    public boolean hasRole(String roleName) {
        return userRoles.stream()
                .anyMatch(ur -> ur.getRole().getName().equals(roleName));
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_USERNAME);
        }
        if (username.length() > 255) {
            throw new BusinessException(ErrorCode.OVER_MAX_LENGTH);
        }
        this.username = username;
    }

    public Location getLocation() { return location; }
    public void setLocation(Location location) {
        if (location == null) {
            throw new BusinessException(ErrorCode.USER_INVALID_LOCATION_SELECTION);
        }
        this.location = location;
    }

    public College getCollege() { return college; }     
    public void setCollege(College college) {
        if (college == null) {
            throw new BusinessException(ErrorCode.USER_INVALID_PROGRAM);
        }
        this.college = college;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_EMAIL);
        }
        if (email.length() > 320) {
            throw new BusinessException(ErrorCode.OVER_MAX_LENGHT_EMAIL);
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL);
        }
        this.email = email;
    }

    public String getProgram() { return program; }
    public void setProgram(String program) {
        if (program == null || program.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.USER_EMPTY_PROGRAM);
        }
        this.program = program;
    }

    public Set<EventRegistration> getRegistrations() { return registrations; }
    public void setRegistrations(Set<EventRegistration> registrations) { this.registrations = registrations; }

    public Set<UserRole> getUserRoles() { return userRoles; }
    public void setUserRoles(Set<UserRole> userRoles) { this.userRoles = userRoles; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }

    public boolean isProfileCompleted() { return profileCompleted; }
    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", location=" + (location != null ? location.getName() : "null") +
                ", college=" + (college != null ? college.getName() : "null") +
                ", program='" + program + '\'' +
                ", email='" + email + '\'' +
                ", registrations.size=" + (registrations != null ? registrations.size() : "0") +
                ", roles.size=" + (userRoles != null ? userRoles.size() : "0") +
                ", profileCompleted=" + profileCompleted +
                '}';
    }
}