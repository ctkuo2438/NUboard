package com.neu.nuboard.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.nuboard.dto.UserCreateDTO;
import com.neu.nuboard.model.User;
import com.neu.nuboard.service.UserService;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 获取所有用户
     * 本质：把一组User对象转换成一组Map对象。
     * @return 用户列表
     * 1.ResponseEntity是spring框架的类，用来封装http的响应（状态码、头信息和响应体）
     * 2.List<Map<String, Object>>表示响应体是一个列表，列表中每个元素是一个键值对映射
     * 3.业务处理逻辑，调用userService的getAllUsers方法获取所有用户
     * 4.数据转换，将User实体转换为Map<String, Object>
     * 5.collect(Collectors.toList())将转换后的列表收集为一个列表
     * 6.return new ResponseEntity<>(response, HttpStatus.OK)返回响应实体，状态码为200
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<Map<String, Object>> response = users.stream()
            .map(this::convertUserToResponse)
            .collect(Collectors.
            toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        Map<String, Object> response = convertUserToResponse(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * 创建新用户
     * @param userDTO 用户创建DTO
     * @return 创建成功的用户
     * 
     * Restful Api端点，接收前端提交的用户数据并创建新用户
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Validated @RequestBody UserCreateDTO userDTO) {
        User createdUser = userService.createUser(userDTO);
        Map<String, Object> response = convertUserToResponse(createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * 修改用户信息
     * @param id 用户ID
     * @param userDTO 用户信息DTO
     * @return 更新后的用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String id, @Validated @RequestBody UserCreateDTO userDTO) {
        User updatedUser = userService.updateUser(id, userDTO);
        Map<String, Object> response = convertUserToResponse(updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 无内容响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /**
     * 搜索用户，支持通过用户名或邮箱搜索
     * @param username 用户名关键字（可选）
     * @param email 邮箱关键字（可选）
     * @return 匹配的用户列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email) {
        try {
            // 参数验证
            if ((username == null || username.trim().isEmpty()) && 
                (email == null || email.trim().isEmpty())) {
                throw new BusinessException(ErrorCode.SEARCH_KEYWORD_EMPTY);
            }
            
            // 优先使用username参数
            String keyword = (username != null && !username.trim().isEmpty()) ? username : email;
            
            List<User> users = userService.searchUsers(keyword);
            List<Map<String, Object>> response = users.stream()
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("username", user.getUsername());
                    userMap.put("email", user.getEmail());
                    return userMap;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    /**
     * 将User实体转换为响应对象
     * @param user 用户实体
     * @return 响应Map
     */
    private Map<String, Object> convertUserToResponse(User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("program", user.getProgram());
        response.put("location", user.getLocation().getName());
        response.put("college", user.getCollege().getName());
        
        if (user.getEvents() != null && !user.getEvents().isEmpty()) {
            response.put("eventsCount", user.getEvents().size());
        } else {
            response.put("eventsCount", 0);
        }
        
        return response;
    }
}