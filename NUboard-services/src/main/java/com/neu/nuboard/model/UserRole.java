package com.neu.nuboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "role_id"})
        }
)
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by")
    private String assignedBy;

    // Default constructor
    public UserRole() {
        this.assignedAt = LocalDateTime.now();
    }

    // Constructor with user and role
    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.assignedAt = LocalDateTime.now();
    }

    // Constructor with all fields
    public UserRole(User user, Role role, String assignedBy) {
        this.user = user;
        this.role = role;
        this.assignedBy = assignedBy;
        this.assignedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return user.equals(userRole.user) && role.equals(userRole.role);
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(user, role);
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : null) +
                ", role=" + (role != null ? role.getName() : null) +
                ", assignedAt=" + assignedAt +
                ", assignedBy='" + assignedBy + '\'' +
                '}';
    }
}
