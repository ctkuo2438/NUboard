package com.neu.nuboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.neu.nuboard.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // check if username already exists
    boolean existsByUsername(String username);
    
    // check if email already exists
    boolean existsByEmail(String email);

    // find user by email
    Optional<User> findByEmail(String email);

    // find user by username
    Optional<User> findByUsername(String username);

    // fuzzy search users by username or email
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("keyword") String keyword);

    // ===== New methods for a role system =====
    // Find users by role name
    @Query("SELECT DISTINCT u FROM User u JOIN u.userRoles ur WHERE ur.role.name = :roleName")
    List<User> findUsersByRoleName(@Param("roleName") String roleName);

    // Find users by role ID
    @Query("SELECT DISTINCT u FROM User u JOIN u.userRoles ur WHERE ur.role.id = :roleId")
    List<User> findUsersByRoleId(@Param("roleId") Long roleId);

    // Find users who have a specific permission
    @Query("SELECT DISTINCT u FROM User u JOIN u.userRoles ur JOIN ur.role.rolePermissions rp WHERE rp.permission.name = :permissionName")
    List<User> findUsersByPermissionName(@Param("permissionName") String permissionName);

    // Check if user has a specific role
    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.name = :roleName")
    boolean userHasRole(@Param("userId") Long userId, @Param("roleName") String roleName);

    // Check if user has a specific permission
    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur JOIN ur.role.rolePermissions rp WHERE ur.user.id = :userId AND rp.permission.name = :permissionName")
    boolean userHasPermission(@Param("userId") Long userId, @Param("permissionName") String permissionName);

    // Find users without any roles
    @Query("SELECT u FROM User u WHERE u.userRoles IS EMPTY")
    List<User> findUsersWithoutRoles();

    // Find enabled/disabled users
    List<User> findByEnabled(boolean enabled);

    // Find users by auth provider
    List<User> findByAuthProvider(String authProvider);

    // Count users by role
    @Query("SELECT COUNT(DISTINCT u) FROM User u JOIN u.userRoles ur WHERE ur.role.name = :roleName")
    Long countUsersByRoleName(@Param("roleName") String roleName);


}