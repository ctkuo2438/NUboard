package com.neu.nuboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.neu.nuboard.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 检查用户名是否已存在
    boolean existsByUsername(String username);
    
    // 检查电子邮件是否已存在
    boolean existsByEmail(String email);
    
    // 根据电子邮件查找用户
    Optional<User> findByEmail(String email);

    // 根据用户名或邮箱模糊搜索用户
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("keyword") String keyword, @Param("keyword") String email);
} 