package com.neu.nuboard.dto;

import com.neu.nuboard.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthorizationResponseMapper {

    public UserRoleResponseDTO toUserRoleResponse(User user) {
        return new UserRoleResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProgram(),
                user.isEnabled(),
                toLocationResponseDTO(user.getLocation()),
                toCollegeResponseDTO(user.getCollege()),
                user.getAuthProvider(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()),
                user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(Permission::getName)
                        .collect(Collectors.toSet())
        );
    }

    public RoleResponseDTO toRoleResponse(Role role, Long userCount) {
        return new RoleResponseDTO(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getPermissions().stream()
                        .map(this::toPermissionResponse)
                        .collect(Collectors.toList()),
                userCount
        );
    }

    public PermissionResponseDTO toPermissionResponse(Permission permission) {
        return new PermissionResponseDTO(
                permission.getId(),
                permission.getName(),
                permission.getDescription()
        );
    }

    public PermissionWithRolesResponseDTO toPermissionWithRolesResponse(Permission permission) {
        return new PermissionWithRolesResponseDTO(
                permission.getId(),
                permission.getName(),
                permission.getDescription(),
                permission.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        );
    }

    public RoleActivityResponseDTO toRoleActivityResponse(UserRole userRole) {
        return new RoleActivityResponseDTO(
                userRole.getUser().getUsername(),
                userRole.getRole().getName(),
                userRole.getAssignedAt(),
                userRole.getAssignedBy()
        );
    }

    public UserStatusResponseDTO toUserStatusResponse(User user) {
        return new UserStatusResponseDTO(
                user.getId(),
                user.getUsername(),
                user.isEnabled()
        );
    }

    // Helper methods
    private LocationResponseDTO toLocationResponseDTO(Location location) {
        if (location == null) return null;
        return new LocationResponseDTO(
                location.getId(),
                location.getName()
        );
    }

    private CollegeResponseDTO toCollegeResponseDTO(College college) {
        if (college == null) return null;
        return new CollegeResponseDTO(
                college.getId(),
                college.getName()
        );
    }
}
