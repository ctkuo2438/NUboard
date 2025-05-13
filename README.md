# NUboard projecrt
## Team members: Liu, kiki, Jinxiang, Andy

- Frontend: React.js
- Backend: Java + Spring Boot
- DB: postgresql

## Week1 proress: Event Service

- POST /api/events: Create a new event.
- GET /api/events: List all events.
- POST /api/events/{id}/register: Register for an event.

## Technologies Used

*   **Language**: Java 17
*   **Framework**: Spring Boot
    *   Spring Web (for RESTful APIs)
    *   Spring Data JPA (for database interaction)
*   **Database**: PostgreSQL
*   **Build Tool**: Gradle

## Running for Development

To build and run the project in one step, navigate to the root directory and use the Spring Boot Gradle plugin's `bootRun` task:

```bash
./gradlew bootRun
```
The application will typically be accessible at `http://localhost:8080`.

## API Endpoints

### Create a New Event

- **Endpoint**: `POST /api/events`
- **Description**: Creates a new event.
- **Request Body**: `EventCreateDTO`
```
    {
        "title": "Spring Boot Workshop",
        "description": "A workshop on developing applications with Spring Boot.",
        "eventDate": "2024-08-15T10:00:00", // ISO 8601 LocalDateTime format
        "location": "Online",
        "creatorId": "user-uuid-123"
    }
```
- **Response**: `201 CREATED` with `EventResponseDTO`
```
    {
        "id": "event-uuid-abc",
        "title": "Spring Boot Workshop",
        "description": "A workshop on developing applications with Spring Boot.",
        "eventDate": "2024-08-15T10:00:00",
        "location": "Online",
        "creatorId": "user-uuid-123",
        "participants": []
    }
```
### Get All Events

- **Endpoint**: `GET /api/events`
- **Description**: Retrieves a list of all events.
- **Response**: `200 OK` with `List<EventResponseDTO>`

### Register a User for an Event

- **Endpoint**: `POST /api/events/{id}/register`
- **Path Variable**: - The UUID of the event. `id`
- **Description**: Registers a user (identified by their ID) for the specified event.
- **Request Body**: (user ID) `String`

## Project Structure

Key components of the project include:

*   `com.neu.nuboard.controller.EventController`: Handles incoming HTTP requests for event operations.
*   `com.neu.nuboard.service.EventService`: Contains the business logic for event management.
*   `com.neu.nuboard.model.Event`: JPA entity representing an event.
*   `com.neu.nuboard.model.User`: JPA entity representing a user.
*   `com.neu.nuboard.repository.EventRepository`: Spring Data JPA repository for the `Event` entity.
*   `com.neu.nuboard.dto`: Contains Data Transfer Objects (`EventCreateDTO`, `EventResponseDTO`) for API communication.
*   `com.neu.nuboard.exception.GlobalExceptionHandler`: Handles exceptions globally and provides consistent error responses.
*   `com.neu.nuboard.exception.EventNotFoundException`: Custom exception thrown when an event is not found.
*   `com.neu.nuboard.utils.UUIDutil`: Utility for generating UUIDs.







