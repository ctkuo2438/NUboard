package com.neu.nuboard.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.utils.UUIDutil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "users")
public class User {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @Column(name = "program", nullable = false)
    private String program;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventRegistration> registrations = new HashSet<>();

    /**
     * JPA要求的默认无参构造函数。
     */
    protected User() {
        this.id = UUIDutil.getId();
    }

    /**
     * 创建一个完整参数的用户构造函数。
     *
     * @param username 用户名。
     * @param program 用户所学专业。
     * @param email 用户邮箱。
     */
    public User(String username, String program, String email) {
        this.id = UUIDutil.getId();
        this.setUsername(username);
        this.setProgram(program);
        this.setEmail(email);
    }
    
    // Getters
    public UUID getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_USERNAME);
        }
        if (username.length() > 255) {
            throw new BusinessException(ErrorCode.OVER_MAX_LENGTH);
        }
        this.username = username;
    }

    public Location getLocation() { return location; }
    public void setLocation(Location location) {
        if (location == null) {
            throw new BusinessException(ErrorCode.USER_INVALID_LOCATION_SELECTION);
        }
        this.location = location;
    }

    public College getCollege() { return college; }     
    public void setCollege(College college) {
        if (college == null) {
            throw new BusinessException(ErrorCode.USER_INVALID_PROGRAM);
        }
        this.college = college;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_EMAIL);
        }
        if (email.length() > 320) {
            throw new BusinessException(ErrorCode.OVER_MAX_LENGHT_EMAIL);
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL);
        }
        this.email = email;
    }

    public String getProgram() { return program; }
    public void setProgram(String program) {
        if (program == null || program.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.USER_EMPTY_PROGRAM);
        }
        this.program = program;
    }

    public Set<EventRegistration> getRegistrations() { return registrations; }
    public void setRegistrations(Set<EventRegistration> registrations) { this.registrations = registrations; }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", location=" + location +
                ", college=" + college +
                ", program='" + program + '\'' +
                ", email='" + email + '\'' +
                ", registrations.size=" + (registrations != null ? registrations.size() : "0") +
                '}';
    }
}