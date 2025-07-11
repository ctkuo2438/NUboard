package com.neu.nuboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "role_permissions",
        // bridge-table between the Role and permission table,
        // the role_id and permission_id are the foreign key and primary key of the role_permission table.
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"role_id", "permission_id"})
        }
)
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Default constructor
    public RolePermission() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for adding a permission to the role
    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Permission getPermission() { return permission; }
    public void setPermission(Permission permission) { this.permission = permission; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Checking if two RolePermission objects represent the same single relationship,
     * Preventing duplicate relationships in collections, ensuring proper behavior in HashSet.
     */
    @Override
    public boolean equals(Object o) {
        // check if comparing with itself (same memory reference)
        if (this == o) return true;
        // check if other objects are null or different class
        if (o == null || getClass() != o.getClass()) return false;
        RolePermission that = (RolePermission) o;
        // same role and permission?
        return role.equals(that.role) && permission.equals(that.permission);
    }
    // If two objects are equal according to equals(), they must have the same hashCode().
    @Override
    public int hashCode() {
        return java.util.Objects.hash(role, permission);
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "id=" + id +
                ", role=" + (role != null ? role.getName() : null) +
                ", permission=" + (permission != null ? permission.getName() : null) +
                ", createdAt=" + createdAt +
                '}';
    }
}
