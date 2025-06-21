package com.neu.nuboard.controller;

import com.neu.nuboard.dto.LocationResponseDTO;
import com.neu.nuboard.exception.SuccessResponse;
import com.neu.nuboard.model.Location;
import com.neu.nuboard.repository.LocationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true") // Make data transmit between different servers.
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationRepository locationRepository;

    public LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // Return all locations to the frontend.
    @GetMapping
    public SuccessResponse<List<LocationResponseDTO>> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        List<LocationResponseDTO> locationDTOs = locations.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new SuccessResponse<>(locationDTOs);
    }

    // Entity → DTO
    private LocationResponseDTO convertToDTO(Location entity) {
        return new LocationResponseDTO(entity.getId(), entity.getName());
    }
}