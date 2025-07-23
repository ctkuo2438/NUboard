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
    // 检查用户名是否已存在
    boolean existsByUsername(String username);
    
    // 检查电子邮件是否已存在
    boolean existsByEmail(String email);
    
    // 根据电子邮件查找用户
    Optional<User> findByEmail(String email);

    // 根据用户名查找用户
    Optional<User> findByUsername(String username);

    // 根据用户名或邮箱模糊搜索用户
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