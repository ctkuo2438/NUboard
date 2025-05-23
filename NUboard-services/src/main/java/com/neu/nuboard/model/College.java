package com.neu.nuboard.model;

import jakarta.persistence.*;

import java.util.List;

// College entity doesn't have corresponding controller or repo, so we insert data manually by .sql file in main/resources.
@Entity
@Table(name = "colleges")
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    // college is the filed name in User table
    // User is the name of the Entity Class
    @OneToMany(mappedBy = "college")
    private List<User> users;

    //Getter
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    // Setter
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
