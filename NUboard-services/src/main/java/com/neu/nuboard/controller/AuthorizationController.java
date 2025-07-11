package com.neu.nuboard.controller;

import com.neu.nuboard.dto.*;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Purpose: Administrative operations
 * REST controller for handling admin-related HTTP requests.
 * All endpoints require an ADMIN role and provide administrative functionality
 * for managing users, roles, permissions, and system statistics.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@PreAuthorize("hasRole('ADMIN')") // All endpoints require ADMIN role
public class AuthorizationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    private final AuthorizationService adminService;

    /**
     * Constructs an AdminController with the specified AdminService.
     *
     * @param adminService the service to handle admin-related business logic
     */
    @Autowired
    public AuthorizationController(AuthorizationService adminService) {
        this.adminService = adminService;
    }

    // Role Management
    /**
     * GET /api/admin/roles
     * Get all roles with their permissions. -> adminService.getAllRoles()
     * Role Management UI: What permissions does the USER role have?
     *
     * @return ResponseEntity containing all roles with their permissions and user counts
     */
    @GetMapping("/roles")
    public ResponseEntity<SuccessResponse<List<RoleResponseDTO>>> getAllRoles() {
        List<RoleResponseDTO> roles = adminService.getAllRoles();
        return ResponseEntity.ok(new SuccessResponse<>(roles));
    }

    /**
     * GET /api/admin/permissions
     * Get all permissions. -> adminService.getAllPermissions()
     * Permission Audit UI: Which roles have the dangerous DELETE_USERS permission?
     *
     * @return ResponseEntity containing all permissions with their associated roles
     */
    @GetMapping("/permissions")
    public ResponseEntity<SuccessResponse<List<PermissionWithRolesResponseDTO>>> getAllPermissions() {
        List<PermissionWithRolesResponseDTO> permissions = adminService.getAllPermissions();
        return ResponseEntity.ok(new SuccessResponse<>(permissions));
    }

    // User Role Management
    /**
     * POST /users/{userId}/roles
     * Assign a role to a user. -> adminService.assignRoleToUser()
     *
     * @param userId the ID of the user to assign the role to
     * @param request the request body containing the role name
     * @return ResponseEntity containing the updated user role information
     */
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<SuccessResponse<UserRoleResponseDTO>> assignRoleToUser(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {

        String roleName = request.get("roleName");
        String assignedByUsername = "system";

        UserRoleResponseDTO response = adminService.assignRoleToUser(userId, roleName, assignedByUsername);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    /**
     * DELETE /users/{userId}/roles/{roleName}
     * Remove a role from a user.
     *
     * @param userId the ID of the user to remove the role from
     * @param roleName the name of the role to remove
     * @return ResponseEntity containing the updated user role information
     */
    @DeleteMapping("/users/{userId}/roles/{roleName}")
    public ResponseEntity<SuccessResponse<UserRoleResponseDTO>> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable String roleName) {

        String removedByUsername = "system";
        UserRoleResponseDTO response = adminService.removeRoleFromUser(userId, roleName, removedByUsername);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    /**
     * GET /users/roles
     * Get all users with their roles.
     *
     * @return ResponseEntity containing all users with their roles and permissions
     */
    @GetMapping("/users/roles")
    public ResponseEntity<SuccessResponse<List<UserRoleResponseDTO>>> getAllUsersWithRoles() {
        List<UserRoleResponseDTO> users = adminService.getAllUsersWithRoles();
        return ResponseEntity.ok(new SuccessResponse<>(users));
    }

    /**
     * GET /roles/{roleName}/users
     * Get users by role. -> adminService.getUsersByRole()
     *
     * @param roleName the name of the role to filter users by
     * @return ResponseEntity containing users with the specified role
     */
    @GetMapping("/roles/{roleName}/users")
    public ResponseEntity<SuccessResponse<List<UserRoleResponseDTO>>> getUsersByRole(
            @PathVariable String roleName) {

        List<UserRoleResponseDTO> users = adminService.getUsersByRole(roleName);
        return ResponseEntity.ok(new SuccessResponse<>(users));
    }

    // System Statistics
    /**
     * GET /statistics
     * Get system statistics. -> adminService.getSystemStatistics()
     *
     * @return ResponseEntity containing comprehensive system statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> getSystemStatistics() {
        Map<String, Object> stats = adminService.getSystemStatistics();
        return ResponseEntity.ok(new SuccessResponse<>(stats));
    }

    /**
     * GET /activity/roles
     * Get system activity logs (recent role assignments). -> adminService.getRecentRoleActivity()
     *
     * @return ResponseEntity containing recent role assignment activity
     */
    @GetMapping("/activity/roles")
    public ResponseEntity<SuccessResponse<List<RoleActivityResponseDTO>>> getRecentRoleActivity() {
        List<RoleActivityResponseDTO> activities = adminService.getRecentRoleActivity();
        return ResponseEntity.ok(new SuccessResponse<>(activities));
    }

    // User Management
    /**
     * PUT /users/{userId}/status
     * Enable or disable a user account. -> adminService.updateUserStatus()
     *
     * @param userId the ID of the user to update
     * @param request the request body containing the enabled status
     * @return ResponseEntity containing the updated user status
     */
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<SuccessResponse<UserStatusResponseDTO>> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody Map<String, Boolean> request) {

        Boolean enabled = request.get("enabled");

        Long currentUserId = userId;
        String updatedByUsername = "system";
        UserStatusResponseDTO response = adminService.updateUserStatus(
                userId, enabled, currentUserId, updatedByUsername);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    /**
     * GET /users/search
     * To Search users across multiple fields. -> adminService.searchUsers()
     *
     * @param keyword optional keyword to search in username/email
     * @param role optional role name to filter by
     * @param enabled optional enabled status to filter by
     * @return ResponseEntity containing filtered users
     */
    @GetMapping("/users/search")
    public ResponseEntity<SuccessResponse<List<UserRoleResponseDTO>>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled) {

        List<UserRoleResponseDTO> users = adminService.searchUsers(keyword, role, enabled);
        return ResponseEntity.ok(new SuccessResponse<>(users));
    }

    @GetMapping("/test-setup")
    // No @PreAuthorize annotation - this endpoint is public for testing
    public ResponseEntity<Map<String, Object>> testSetup() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "AdminController is working");
        response.put("timestamp", System.currentTimeMillis());

        try {
            List<RoleResponseDTO> roles = adminService.getAllRoles();
            response.put("roles_count", roles.size());
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}