package com.neu.nuboard.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.nuboard.dto.UserCreateDTO;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.model.User;
import com.neu.nuboard.service.UserService;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:80"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets all users. This endpoint is for admin use only.
     * @return A list of all users.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<SuccessResponse<List<UserCreateDTO>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserCreateDTO> response = users.stream()
                .map(this::convertUserToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    /**
     * Gets a user by their ID.
     * A user can view their own information, while an admin can view any user's information.
     * @param id The user's ID.
     * @return The user's information.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_VIEW') or #id == authentication.principal.id.toString()")
    public ResponseEntity<SuccessResponse<UserCreateDTO>> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        UserCreateDTO response = convertUserToDTO(user);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    /**
     * Creates a new user. This endpoint is for admin use only.
     * @param userDTO The user creation DTO.
     * @return The successfully created user.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<UserCreateDTO>> createUser(@Validated @RequestBody UserCreateDTO userDTO) {
        User createdUser = userService.createUser(userDTO);
        UserCreateDTO response = convertUserToDTO(createdUser);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    /**
     * Updates a user's information.
     * A user can update their own information, while an admin can update any user's information.
     * @param id The user's ID.
     * @param userDTO The user information DTO.
     * @return The updated user.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE') or @securityService.isOwner(#id, authentication.principal)")
    public ResponseEntity<SuccessResponse<UserCreateDTO>> updateUser(@PathVariable String id, @Validated @RequestBody UserCreateDTO userDTO) {
        User updatedUser = userService.updateUser(id, userDTO);
        UserCreateDTO response = convertUserToDTO(updatedUser);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    /**
     * Deletes a user. This endpoint is for admin use only.
     * @param id The user's ID.
     * @return A response with no content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Searches for users. This endpoint is for admin use only.
     * @param username The username keyword (optional).
     * @param email The email keyword (optional).
     * @return A list of matching users.
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<SuccessResponse<List<UserCreateDTO>>> searchUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email) {
        try {
            if ((username == null || username.trim().isEmpty()) &&
                    (email == null || email.trim().isEmpty())) {
                throw new BusinessException(ErrorCode.USER_SEARCH_KEYWORD_EMPTY);
            }
            String keyword = (username != null && !username.trim().isEmpty()) ? username : email;
            List<User> users = userService.searchUsers(keyword);
            List<UserCreateDTO> response = users.stream()
                    .map(this::convertUserToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new SuccessResponse<>(response));
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * Converts a User entity to a response DTO.
     * @param user The User entity.
     * @return The response DTO.
     */
    private UserCreateDTO convertUserToDTO(User user) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setId(String.valueOf(user.getId()));
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProgram(user.getProgram());
        dto.setLocationId(user.getLocation() != null ? user.getLocation().getId() : null);
        dto.setCollegeId(user.getCollege() != null ? user.getCollege().getId() : null);
        dto.setLocationName(user.getLocation() != null ? user.getLocation().getName() : null);
        dto.setCollegeName(user.getCollege() != null ? user.getCollege().getName() : null);
        dto.setEventsCount(user.getRegistrations() != null ? user.getRegistrations().size() : 0);
        return dto;
    }
}
