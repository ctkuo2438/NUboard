package com.neu.nuboard.repository;

import com.neu.nuboard.model.RolePermission;
import com.neu.nuboard.model.Role;
import com.neu.nuboard.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    /** method name conventions, implement by JPA */
    // Find all permissions for a specific role
    List<RolePermission> findByRole(Role role);
    // Find all permissions for a role by role ID
    List<RolePermission> findByRoleId(Long roleId);

    // Find all roles that have a specific permission
    List<RolePermission> findByPermission(Permission permission);
    // Find all roles that have a permission by permission ID
    List<RolePermission> findByPermissionId(Long permissionId);

    // Check if a role-permission combination exists, preventing duplicate relationships
    boolean existsByRoleAndPermission(Role role, Permission permission);
    // Check if a role-permission combination exists by IDs
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

    // Find a specific role-permission combination
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
    // Find a specific role-permission combination by IDs
    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    // Delete role-permission relationship
    void deleteByRoleAndPermission(Role role, Permission permission);
    // Delete all permissions for a role
    void deleteByRole(Role role);

    // Delete a specific permission from ALL roles that currently have it.
    // It's used when you want to completely eliminate a permission from your system.
    void deleteByPermission(Permission permission);

    // Find role permissions created after a certain date
    List<RolePermission> findByCreatedAtAfter(LocalDateTime date);

    // Count permissions for a specific role
    Long countByRole(Role role);

    // Count roles that have a specific permission
    Long countByPermission(Permission permission);

    /** custom query methods */
    // Check if a role has a specific permission by names
    @Query("SELECT COUNT(rp) > 0 FROM RolePermission rp WHERE rp.role.name = :roleName AND rp.permission.name = :permissionName")
    boolean roleHasPermission(@Param("roleName") String roleName, @Param("permissionName") String permissionName);

    // Find all permissions for a role by role name
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.name = :roleName")
    List<RolePermission> findByRoleName(@Param("roleName") String roleName);
}
