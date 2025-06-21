package com.neu.nuboard.controller;

import com.neu.nuboard.dto.CollegeResponseDTO;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.model.College;
import com.neu.nuboard.repository.CollegeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@RestController
@RequestMapping("/api/colleges")
public class CollegeController {

    private final CollegeRepository collegeRepository;

    public CollegeController(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    // Return all colleges to the frontend.
    @GetMapping
    public SuccessResponse<List<CollegeResponseDTO>> getAllLocations() {
        List<College> colleges = collegeRepository.findAll();
        List<CollegeResponseDTO> collegeDTOs = colleges.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new SuccessResponse<>(collegeDTOs);
    }

    // Entity → DTO
    private CollegeResponseDTO convertToDTO(College entity) {
        return new CollegeResponseDTO(entity.getId(), entity.getName());
    }
}