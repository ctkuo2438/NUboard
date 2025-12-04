package com.neu.nuboard.dto;
/**
 * DTO (Data Transfer Object) for user creation and response.
 * This object is used to transfer user-related data between different layers of the application,
 * such as from the client to the server for requests, and from the server to the client for responses.
 *
 * <h2>Usage Flow:</h2>
 * <ol>
 *   <li><b>Receiving Request Data:</b> When a user submits a form (e.g., registration), the data is converted into a {@code UserCreateDTO} object in the controller.</li>
 *   <li><b>Converting to Business Object:</b> The DTO is then converted into a {@code User} entity within the service layer for business logic processing.</li>
 *   <li><b>Converting to Response Data:</b> The {@code User} entity is converted back into a {@code UserCreateDTO} in the controller to be sent as a response.</li>
 *   <li><b>Returning Response to Client:</b> The DTO is serialized into JSON and sent back to the client.</li>
 * </ol>
 */
public class UserCreateDTO {
    // Fields for receiving requests
    private String username;
    private String email;
    private String program;   
    private Long locationId;    
    private Long collegeId;    
    // Fields for responses, returned to the client from the UserController
    private String id;
    private String locationName;
    private String collegeName;
    private int eventsCount;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public Long getCollegeId() { return collegeId; }
    public void setCollegeId(Long collegeId) { this.collegeId = collegeId; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

    public int getEventsCount() { return eventsCount; }
    public void setEventsCount(int eventsCount) { this.eventsCount = eventsCount; }
}                       
