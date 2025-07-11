package com.neu.nuboard.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Permission entity represents an individual permission in the system,
 * such as "READ_POST", "EDIT_USER", etc.
 * Linked to roles via RolePermission (bridge table).
 */
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    // handle M:M relationship between Role and Permission
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> rolePermissions = new HashSet<>();

    // Constructors
    // Default constructor for JPA
    public Permission() {}

    // constructor for the convenient programming
    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Helper method to get roles that have this permission
    /**
     * get all roles that include this permission.
     * what roles have this permission?
     * useful for admin dashboards or access checks.
     */
    public Set<Role> getRoles() {
        return rolePermissions.stream()
                .map(RolePermission::getRole)
                .collect(Collectors.toSet());
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<RolePermission> getRolePermissions() { return rolePermissions; }
    public void setRolePermissions(Set<RolePermission> rolePermissions) { this.rolePermissions = rolePermissions; }
}
