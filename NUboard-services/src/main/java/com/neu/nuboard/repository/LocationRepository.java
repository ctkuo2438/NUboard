package com.neu.nuboard.repository;

import com.neu.nuboard.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}