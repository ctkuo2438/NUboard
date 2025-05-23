package com.neu.nuboard.repository;

import com.neu.nuboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide CRUD operations for the users table.
 */
public interface UserRepository extends JpaRepository<User, String> {
}
