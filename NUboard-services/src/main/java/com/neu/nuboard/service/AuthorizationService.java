package com.neu.nuboard.service;

import com.neu.nuboard.dto.*;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.model.Permission;
import com.neu.nuboard.model.Role;
import com.neu.nuboard.model.User;
import com.neu.nuboard.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing admin-related business logic.
 * Provides methods for role management, user management, and system statistics.
 */
@Service
@Transactional
public class AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    // Role constants
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final AuthorizationResponseMapper mapper;

    /**
     * Constructs an AdminService with the specified repositories and mapper.
     *
     * @param userRepository the repository for user persistence operations must not be null
     * @param roleRepository the repository for role persistence operations must not be null
     * @param permissionRepository the repository for permission persistence operations must not be null
     * @param userRoleRepository the repository for user-role relationship operations must not be null
     * @param eventRepository the repository for event persistence operations must not be null
     * @param eventRegistrationRepository the repository for event registration operations must not be null
     */
    @Autowired
    public AuthorizationService(
            @NonNull UserRepository userRepository,
            @NonNull RoleRepository roleRepository,
            @NonNull PermissionRepository permissionRepository,
            @NonNull UserRoleRepository userRoleRepository,
            @NonNull EventRepository eventRepository,
            @NonNull EventRegistrationRepository eventRegistrationRepository,
            @NonNull AuthorizationResponseMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.mapper = mapper;
    }

    // Role Management
    /**
     * Retrieves all roles with their permissions and user counts.
     *
     * @return a list of role DTOs containing role information
     * @throws BusinessException if database operation fails
     */
    public List<RoleResponseDTO> getAllRoles() {  // ← Changed return type
        try {
            List<Role> roles = roleRepository.findAllByOrderByNameAsc();
            return roles.stream()
                    .map(role -> mapper.toRoleResponse(role, userRoleRepository.countByRole(role)))  // ← Use mapper
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to retrieve all roles", e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    // Permission Management
    /**
     * Retrieves all permissions with their associated roles.
     *
     * @return a list of permission DTOs containing permission information
     * @throws BusinessException if database operation fails
     */
    public List<PermissionWithRolesResponseDTO> getAllPermissions() {
        try {
            List<Permission> permissions = permissionRepository.findAllByOrderByNameAsc();
            return permissions.stream()
                    .map(mapper::toPermissionWithRolesResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to retrieve all permissions", e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    // User Role Management
    /**
     * Assigns a role to a user.
     *
     * @param userId the ID of the user must not be null
     * @param roleName the name of the role to assign, must not be null or empty
     * @param assignedByUsername the username of the admin performing the action must not be null or empty, like "admin_sarah"
     * @return a DTO containing updated user role information
     * @throws BusinessException if the role name is invalid, user/role not found, or user already has the role
     */
    public UserRoleResponseDTO assignRoleToUser(@NonNull Long userId, @NonNull String roleName, @NonNull String assignedByUsername) {
        // Validate inputs, if roleName was "" or null, throw BusinessException
        validateStringParameter(roleName, "Role name is required");
        validateStringParameter(assignedByUsername, "Assigned by username is required");

        // Database entity Lookup, find user and role
        // findById: SELECT * FROM users WHERE id = 123;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        // findByName: SELECT * FROM roles WHERE name = 'MANAGER';
        Role role = roleRepository.findByName(roleName.trim())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        // Check if a user already has this role, false -> Can assign
        if (user.hasRole(roleName)) {
            throw new BusinessException(ErrorCode.USER_ALREADY_HAS_ROLE);
        }

        try {
            // Role Assignment, add the role with audit info
            user.addRole(role);
            // Track who assigned the role
            // user.getUserRoles(): Returns: [UserRole1(USER), UserRole2(MANAGER-just added)]
            user.getUserRoles().stream()
                    .filter(ur -> ur.getRole().equals(role)) // Result: UserRole2(MANAGER)
                    .findFirst() // Get the first (should be only) match. Result: Optional<UserRole2>
                    // If found, set the assignedBy field. Result: UserRole2.assignedBy = "admin_sarah"
                    .ifPresent(ur -> ur.setAssignedBy(assignedByUsername.trim()));
            userRepository.save(user); // Saves User and related UserRole records

            // INFO: Admin admin_sarah assigned role MANAGER to user john_doe
            logger.info("Admin {} assigned role {} to user {}", assignedByUsername, roleName, user.getUsername());
            return mapper.toUserRoleResponse(user); // Creates standardized response, see notion notes

        } catch (Exception e) {
            logger.error("Failed to assign role {} to user {}", roleName, userId, e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * Removes a role from a user.
     *
     * @param userId the ID of the user must not be null
     * @param roleName the name of the role to remove, must not be null or empty
     * @param removedByUsername the username of the admin performing the action must not be null or empty
     * @return a DTO containing updated user role information
     * @throws BusinessException if the role name is invalid, user/role isn't found, or attempting to remove the last admin
     */
    public UserRoleResponseDTO removeRoleFromUser(@NonNull Long userId, @NonNull String roleName, @NonNull String removedByUsername) {
        // Validate inputs
        validateStringParameter(roleName, "Role name is required");
        validateStringParameter(removedByUsername, "Removed by username is required");

        // Find user and role
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Role role = roleRepository.findByName(roleName.trim())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        // Prevent removing last ADMIN role!
        // ADMIN_ROLE.equals(roleName.trim(): true -> trying to remove the ADMIN role
        // userRepository.countUsersByRoleName(ADMIN_ROLE) == 1: Counts how many users currently have the ADMIN role
        // user.hasRole(ADMIN_ROLE): Confirms this specific user actually HAS the ADMIN role
        if (ADMIN_ROLE.equals(roleName.trim()) && userRepository.countUsersByRoleName(ADMIN_ROLE) == 1
                && user.hasRole(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_LAST_ADMIN);
        }

        try {
            user.removeRole(role);
            userRepository.save(user);
            // INFO: Admin super_admin removed role ADMIN from user john_doe
            logger.info("Admin {} removed role {} from user {}", removedByUsername, roleName, user.getUsername());
            return mapper.toUserRoleResponse(user);
        } catch (Exception e) {
            logger.error("Failed to remove role {} from user {}", roleName, userId, e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * Retrieves all users with their roles and permissions.
     *
     * @return a list of maps containing user role information
     * @throws BusinessException if database operation fails
     */
    public List<UserRoleResponseDTO> getAllUsersWithRoles() {
        try {
            List<User> users = userRepository.findAll(); // SELECT * FROM users;
            return users.stream()
                    .map(mapper::toUserRoleResponse) // custom helper method
                    .collect(Collectors.toList()); // Stream<Map<String, Object>> → List<Map<String, Object>>
        } catch (Exception e) {
            logger.error("Failed to retrieve all users with roles", e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * Retrieves users by role name.
     *
     * @param roleName the name of the role must not be null or empty
     * @return a list of user DTOs containing user role information
     * @throws BusinessException if the role name is invalid or database operation fails
     */
    public List<UserRoleResponseDTO> getUsersByRole(@NonNull String roleName) {
        validateStringParameter(roleName, "Role name is required");

        try {
            List<User> users = userRepository.findUsersByRoleName(roleName.trim()); // "  ADMIN  ".trim() → "ADMIN"
            return users.stream()
                    .map(mapper::toUserRoleResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to retrieve users by role {}", roleName, e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    // User Management
    /**
     * Updates a user's enabled/disabled status.
     *
     * @param userId the ID of the user must not be null
     * @param enabled the new enabled status must not be null
     * @param currentUserId the ID of the "admin performing the action" must not be null
     * @param updatedByUsername the username of the admin performing the action must not be null or empty
     * @return a DTO containing updated user status information
     * @throws BusinessException if a user isn't found or attempting to disable the own account
     */
    public UserStatusResponseDTO updateUserStatus(@NonNull Long userId, // WHO to modify (target user)
                                                @NonNull Boolean enabled, // WHAT to change (new status)
                                                @NonNull Long currentUserId, // WHO is doing it (admin's ID)
                                                @NonNull String updatedByUsername) { // WHO is doing it (admin's name)
        if (enabled == null) { // enabled need to be true or false
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        validateStringParameter(updatedByUsername, "Updated by username is required");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        // Prevent disabling yourself
        // if userId=456, currentUserId=456 → true (same person)
        // enabled = false (Sarah wants to SET John's account to disable), !false -> ture
        // Therefore, if both true -> Block the action from disable their own account
        if (userId.equals(currentUserId) && !enabled) {
            throw new BusinessException(ErrorCode.CANNOT_DISABLE_OWN_ACCOUNT);
        }
        // Update User Status
        try {
            user.setEnabled(enabled);
            userRepository.save(user);
            // If enabled = true: "Admin admin_sarah enabled user john_doe"
            // // If enabled = false: "Admin admin_sarah disabled user john_doe"
            logger.info("Admin {} {} user {}", updatedByUsername, enabled ? "enabled" : "disabled", user.getUsername());
            return mapper.toUserStatusResponse(user); // The Response reflects the NEW status after the update.
        } catch (Exception e) {
            logger.error("Failed to update user status for user {}", userId, e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * Searches users based on keyword, role, and enabled status with AND logic.
     *
     * @param keyword optional keyword to search in username/email
     * @param role optional role name to filter by
     * @param enabled optional enabled status to filter by
     * @return a list of user DTOs containing user role information
     * @throws BusinessException if database operation fails
     */
    public List<UserRoleResponseDTO> searchUsers(String keyword, // Search term for username/email (optional)
                                                 String role, // Role name to filter by (optional)
                                                 Boolean enabled) { // Account status to filter by (optional)
        try {
            List<User> users = userRepository.findAll(); // Loads ALL users from database initially
            // Apply filters with AND logic
            return users.stream()
                    .filter(user -> {
                        // Filter by keyword (username or email)
                        if (keyword != null && !keyword.trim().isEmpty()) {
                            String searchTerm = keyword.trim().toLowerCase();
                            return user.getUsername().toLowerCase().contains(searchTerm) ||
                                    user.getEmail().toLowerCase().contains(searchTerm);
                        }
                        return true;
                    })
                    .filter(user -> {
                        // Filter by role
                        if (role != null && !role.trim().isEmpty()) {
                            return user.hasRole(role.trim());
                        }
                        return true;
                    })
                    .filter(user -> {
                        // Filter by enabled status
                        if (enabled != null) {
                            return user.isEnabled() == enabled;
                        }
                        return true;
                    })
                    .map(mapper::toUserRoleResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to search users with keyword: {}, role: {}, enabled: {}", keyword, role, enabled, e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    // System Statistics
    /**
     * Retrieves comprehensive system statistics.
     *
     * @return a map containing various system statistics
     * @throws BusinessException if database operation fails
     */
    public Map<String, Object> getSystemStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // User statistics
            Map<String, Object> userStats = new HashMap<>();
            userStats.put("total", userRepository.count()); // SELECT COUNT(*) FROM users;
            userStats.put("enabled", userRepository.findByEnabled(true).size());
            userStats.put("disabled", userRepository.findByEnabled(false).size());
            // Tracks user type distribution
            userStats.put("byRole", Map.of(
                    USER_ROLE, userRepository.countUsersByRoleName(USER_ROLE),
                    ADMIN_ROLE, userRepository.countUsersByRoleName(ADMIN_ROLE)
            ));
            // Identifies orphaned users
            userStats.put("withoutRoles", userRepository.findUsersWithoutRoles().size());
            stats.put("users", userStats);

            // Registration statistics
            Map<String, Object> regStats = new HashMap<>();
            // Event popularity tracking
            regStats.put("total", eventRegistrationRepository.count());
            stats.put("registrations", regStats);

            // Role statistics
            Map<String, Object> roleStats = new HashMap<>();
            // Get All Roles
            roleRepository.findAll().forEach(role -> {
                roleStats.put(role.getName(), Map.of(
                        // User counts per role
                        // SELECT COUNT(*) FROM user_roles WHERE role_id = ?;
                        "userCount", userRoleRepository.countByRole(role),
                        // Permission counts
                        // SELECT COUNT(*) FROM role_permissions WHERE role_id = ?;
                        "permissionCount", role.getPermissions().size()
                ));
            });
            stats.put("roles", roleStats);
            return stats;

        } catch (Exception e) {
            logger.error("Failed to retrieve system statistics", e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * Retrieves recent role assignment activity.
     *
     * @return a list of role activity DTOs containing recent role activity
     * @throws BusinessException if database operation fails
     */
    public List<RoleActivityResponseDTO> getRecentRoleActivity() {
        try {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

            return userRoleRepository.findByAssignedAtAfter(thirtyDaysAgo)
                    .stream()
                    // Sort by Date (Newest First)
                    .sorted((a, b) -> b.getAssignedAt().compareTo(a.getAssignedAt()))
                    .limit(50)
                    // Transform to Response Format
                    .map(mapper::toRoleActivityResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to retrieve recent role activity", e);
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
    }

    // helper methods
    /**
     * Validates string parameters for null or empty values.
     *
     * @param parameter the string parameter to validate
     * @param errorMessage the error message to use if validation fails
     * @throws BusinessException if parameter is null or empty
     */
    private void validateStringParameter(String parameter, String errorMessage) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, errorMessage);
        }
    }
}