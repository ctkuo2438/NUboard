package com.neu.nuboard.repository;

import com.neu.nuboard.model.UserRole;
import com.neu.nuboard.model.User;
import com.neu.nuboard.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    /** method name conventions, implement by JPA */
    // Find all roles for a user
    List<UserRole> findByUser(User user);
    // Find all roles for a user by user ID
    List<UserRole> findByUserId(Long userId);

    // Find all users with a specific role
    List<UserRole> findByRole(Role role);
    // Find all users with a specific role by role ID
    List<UserRole> findByRoleId(Long roleId);

    // Check if user has a specific role
    boolean existsByUserAndRole(User user, Role role);
    // Check if a user has a specific role by IDs
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

    // Find a specific user-role combination
    Optional<UserRole> findByUserAndRole(User user, Role role);
    // Find a specific user-role combination by IDs
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    // Delete it user-role relationship
    void deleteByUserAndRole(User user, Role role);
    // Delete all roles for a user
    void deleteByUser(User user);
    // Delete all users from a role
    void deleteByRole(Role role);

    // Find user roles assigned after a certain date
    List<UserRole> findByAssignedAtAfter(LocalDateTime date);

    // Find user roles assigned by a specific person
    List<UserRole> findByAssignedBy(String assignedBy);

    // Count users with a specific role
    Long countByRole(Role role);
    // Count roles for a specific user
    Long countByUser(User user);

    /** custom query methods */
    // Find users with a specific role name (using JPQL)
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.name = :roleName")
    List<UserRole> findByRoleName(@Param("roleName") String roleName);

    // Check if a user has a specific role by role name
    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.name = :roleName")
    boolean userHasRole(@Param("userId") Long userId, @Param("roleName") String roleName);
}
