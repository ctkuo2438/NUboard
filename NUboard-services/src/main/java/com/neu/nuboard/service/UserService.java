package com.neu.nuboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neu.nuboard.dto.UserCreateDTO;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.model.College;
import com.neu.nuboard.model.Location;
import com.neu.nuboard.model.User;
import com.neu.nuboard.model.Role;
import com.neu.nuboard.model.UserRole;
import com.neu.nuboard.repository.CollegeRepository;
import com.neu.nuboard.repository.LocationRepository;
import com.neu.nuboard.repository.UserRepository;
import com.neu.nuboard.repository.RoleRepository;
import com.neu.nuboard.repository.UserRoleRepository;
import com.neu.nuboard.utils.SnowflakeIDGenerator;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CollegeRepository collegeRepository;
    private final SnowflakeIDGenerator snowflakeIdGenerator;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    
    @Autowired
    public UserService(UserRepository userRepository, 
                      LocationRepository locationRepository,
                      CollegeRepository collegeRepository,
                      SnowflakeIDGenerator snowflakeIdGenerator,
                      RoleRepository roleRepository,
                      UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.collegeRepository = collegeRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Assign the default USER role to a user
     * @param user the user to assign the role to
     */
    private void assignDefaultRole(User user) {
        // Get the default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        // Create UserRole relationship
        UserRole userRoleRelation = new UserRole(user, userRole, "SYSTEM");

        // Save the user role relationship
        userRoleRepository.save(userRoleRelation);

        // Add to user's role collection
        user.getUserRoles().add(userRoleRelation);
    }
    
    /**
     * acquire all users
     * @return user list
     * @throws BusinessException when database query fails
     */
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.USER_QUERY_FAILED);
        }
    }
    
    /**
     * acquire user by ID
     * @param id user ID
     * @return user
     * @throws BusinessException when user not found
     */
    public User getUserById(String id) {
        return userRepository.findById(Long.valueOf(id))
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public User createUser(UserCreateDTO userDTO) {
        // check if username already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        // check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }  

        // get Location and College entities
        Location location = locationRepository.findById(userDTO.getLocationId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_INVALID_LOCATION_SELECTION));
        College college = collegeRepository.findById(userDTO.getCollegeId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_INVALID_PROGRAM));

        // create user object
        User user = new User(
            snowflakeIdGenerator.nextId(),
            userDTO.getUsername(),
            userDTO.getProgram(),
            userDTO.getEmail()
        );

        // set location and college
        user.setLocation(location);
        user.setCollege(college);
        
        // validate user data
        if (!validateUser(user)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }

        // save user
        User savedUser = userRepository.save(user);

        // Assign the default role to manually created users too
        assignDefaultRole(savedUser);

        return savedUser;
    }
    
    /**
     * update user information
     * @param id user ID
     * @param userDTO user data transfer object
     * @return updated user
     */
    public User updateUser(String id, UserCreateDTO userDTO) {
        // find user to update
        User user = userRepository.findById(Long.valueOf(id))
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        // check if username has changed and exists
        if (!user.getUsername().equals(userDTO.getUsername()) && 
            userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        
        // if email has changed, check if new email exists
        if (!user.getEmail().equals(userDTO.getEmail()) && 
            userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // get Location and College entities
        Location location = locationRepository.findById(userDTO.getLocationId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_INVALID_LOCATION_SELECTION));
        College college = collegeRepository.findById(userDTO.getCollegeId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_INVALID_PROGRAM));
                
        // update user fields
        user.setUsername(userDTO.getUsername());
        user.setProgram(userDTO.getProgram());
        user.setEmail(userDTO.getEmail());
        user.setLocation(location);
        user.setCollege(college);
        
        // validate updated user data
        if (!validateUser(user)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        
        // sava user info and return
        return userRepository.save(user);
    }
    
    /**
     * delete user
     * @param id user ID
     */
    public void deleteUser(String id) {
        // check if user exists
        if (!userRepository.existsById(Long.valueOf(id))) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // delete user
        userRepository.deleteById(Long.valueOf(id));
    }
    
    /**
     * validate user object
     * @param user user object to validate
     * @return true if user is valid, false otherwise
     */
    private boolean validateUser(User user) {
       return user.getUsername() != null && !user.getUsername().trim().isEmpty() &&
               user.getProgram() != null && !user.getProgram().trim().isEmpty() && user.getProgram().length() <= 64 &&
               user.getEmail() != null && !user.getEmail().trim().isEmpty() && user.getEmail().length() <= 320 &&
               user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     *  search users by keyword (username or email)
     * @param keyword search keyword
     * @return list of users matching the keyword
     */
    public List<User> searchUsers(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                throw new BusinessException(ErrorCode.USER_SEARCH_KEYWORD_EMPTY);
            }

            // validate keyword length
            if (keyword.length() > 255) {
                throw new BusinessException(ErrorCode.USER_SEARCH_INVALID_KEYWORD);
            }

            List<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword.trim());

            if (users.isEmpty()) {
                throw new BusinessException(ErrorCode.USER_SEARCH_NO_RESULTS);
            }
            
            return users;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }
} 