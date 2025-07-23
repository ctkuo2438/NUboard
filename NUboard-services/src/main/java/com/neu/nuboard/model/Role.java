package com.neu.nuboard.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * represents a Role in the system. USER, ADMIN
 * each Role can be assigned to multiple users and can have multiple permissions.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Unique name of the role ("ADMIN", "USER")
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    // Role entity (One) To RolePermission entity (Many)
    // cascade = CascadeType.ALL: This means if you save, update, or delete a Role, the same operations will automatically apply to its RolePermissions.
    // orphanRemoval = true: If a RolePermission is removed from the rolePermissions set, it will be automatically deleted from the database.
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> rolePermissions = new HashSet<>(); // prevent duplicates

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    // Default constructor required by JPA
    public Role() {}

    // Constructor for creating new Role objects
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Helper methods for permissions
    /**
     * returns a set of Permission objects associated with this role.
     *
     * how to use:
     * Set<String> permissionNames = adminRole.getPermissions().stream()
     *     .map(Permission::getName)
     *     .collect(Collectors.toSet());
     * result: {"READ_USERS", "WRITE_USERS", "DELETE_USERS"}
     */
    public Set<Permission> getPermissions() {
        return rolePermissions.stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toSet());
    }

    /**
     * adds a permission to this role.
     * automatically links this role to the given permission.
     * rolePermissions is a container that holds many RolePermission objects.
     */
    public void addPermission(Permission permission) {
        // Creates a new RolePermission object that links the current Role (this) with the given Permission we want to add.
        RolePermission rolePermission = new RolePermission(this, permission);
        // Adds this RolePermission object to the rolePermissions Set.
        rolePermissions.add(rolePermission);
    }

    /**
     * removes a permission from this role by comparing Permission equality.
     * rp represents each RolePermission object in the Set.
     * for each RolePermission, it gets the Permission and compares it to the permission we want to remove.
     * Ex: adminRole.removePermission(writePermission)
     * rolePermissions = {
     *     RolePermission(adminRole, readPermission), // rp1
     *     RolePermission(adminRole, writePermission), // rp2
     *     RolePermission(adminRole, deletePermission) } // rp3
     */
    public void removePermission(Permission permission) {
        rolePermissions.removeIf(rp -> rp.getPermission().equals(permission));
    }

    /**
     * checks if this role has a specific permission by name.
     * return type is a boolean
     * rolePermissions.stream() → rp1 → rp2 → rp3 → (operations)
     */
    public boolean hasPermission(String permissionName) {
        return rolePermissions.stream()
                .anyMatch(rp -> rp.getPermission().getName().equals(permissionName));
    }

    // Helper methods for users
    /**
     * get all User objects (John, Jane, Bob) that are assigned to this specific role (Admin).
     * output: Admin: John, Admin: Jane, Admin: Bob
     */
    public Set<User> getUsers() {
        return userRoles.stream()
                .map(UserRole::getUser) // equal to lambda .map(UserRole -> UserRole.getUser())
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

    public Set<UserRole> getUserRoles() { return userRoles; }
    public void setUserRoles(Set<UserRole> userRoles) { this.userRoles = userRoles; }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
