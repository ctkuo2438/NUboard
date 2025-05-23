package com.neu.nuboard.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neu.nuboard.dto.UserCreateDTO;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.model.User;
import com.neu.nuboard.model.Location;
import com.neu.nuboard.model.College;
import com.neu.nuboard.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * 获取所有用户
     * @return 用户列表
     * @throws BusinessException 当数据库查询失败时
     */
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.USER_QUERY_FAILED);
        }
    }
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象
     * @throws BusinessException 如果用户不存在
     */
    public User getUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public User createUser(UserCreateDTO userDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        // 检查电子邮件是否已存在
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }  

        // 创建用户对象
        User user = new User(
            userDTO.getUsername(),
            userDTO.getProgram(),
            userDTO.getEmail()
        );
        
        // 设置location和college
        // 允许同一个college下有多个不同program
        user.setLocation(userDTO.getLocation());
        user.setCollege(userDTO.getCollege());
        
        // 验证用户数据
        if (!validateUser(user)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        
        // 保存用户并返回
        return userRepository.save(user);
    }
    
    /**
     * 更新用户信息
     * @param id 要更新的用户ID
     * @param userDTO 用户信息DTO
     * @return 更新后的用户
     */
    public User updateUser(String id, UserCreateDTO userDTO) {
        // 查找要更新的用户
        User user = userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        // 如果用户名变更了，检查是否已存在
        if (!user.getUsername().equals(userDTO.getUsername()) && 
            userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        
        // 如果邮箱变更了，检查是否已存在
        if (!user.getEmail().equals(userDTO.getEmail()) && 
            userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
                
        // 更新用户信息
        user.setUsername(userDTO.getUsername());
        user.setProgram(userDTO.getProgram());
        user.setEmail(userDTO.getEmail());
        
        // 验证用户数据
        if (!validateUser(user)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        
        // 保存并返回更新后的用户
        return userRepository.save(user);
    }
    
    /**
     * 删除用户
     * @param id 要删除的用户ID
     */
    public void deleteUser(String id) {
        // 检查用户是否存在
        if (!userRepository.existsById(UUID.fromString(id))) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 删除用户
        userRepository.deleteById(UUID.fromString(id));
    }
    
    /**
     * 验证必填字段是否存在且不为空。
     * @param user 要验证的用户对象
     * @return 如果用户有效则返回true，否则返回false。
     */
    private boolean validateUser(User user) {
       return user.getUsername() != null && !user.getUsername().trim().isEmpty() &&
               user.getProgram() != null && !user.getProgram().trim().isEmpty() && user.getProgram().length() <= 64 &&
               user.getEmail() != null && !user.getEmail().trim().isEmpty() && user.getEmail().length() <= 320 &&
               user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * 根据关键字搜索用户，支持用户名或邮箱模糊匹配
     * @param keyword 搜索关键字
     * @return 匹配的用户列表
     */
    public List<User> searchUsers(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                throw new BusinessException(ErrorCode.SEARCH_KEYWORD_EMPTY);
            }
            
            // 验证关键字格式
            if (keyword.length() > 255) {
                throw new BusinessException(ErrorCode.SEARCH_INVALID_KEYWORD);
            }
            
            List<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword.trim(), keyword.trim());
            
            if (users.isEmpty()) {
                throw new BusinessException(ErrorCode.SEARCH_NO_RESULTS);
            }
            
            return users;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }
} 