package com.neu.nuboard.model;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

// Location entity doesn't have corresponding controller or repo, so we insert data manually by .sql file in main/resources.
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // location is the filed name in 'users' table
    // User is the name of the Entity Class
    @OneToMany(mappedBy = "location")
    private List<User> users;

    // location is the filed name in 'events' table
    // Event is the name of the Entity Class
    @OneToMany(mappedBy = "location")
    private List<Event> events;

    // Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Event> getEvents() {
        return events;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
