package com.neu.nuboard.repository;

import com.neu.nuboard.model.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long> {
}