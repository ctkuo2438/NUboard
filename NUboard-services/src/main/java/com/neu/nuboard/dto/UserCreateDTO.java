package com.neu.nuboard.dto;
//DTO: Data Transfer Object，数据传输对象，用于传输数据
//DTO是用于在不同层之间传递数据的对象，通常用于表示从客户端到服务器的请求数据或从服务器返回给客户端的数据
//1. 从客户端接收请求数据 - 例如，当用户提交注册表单时，表单数据会被转换为DTO对象，在Controller层接收客户端数据 
//   使用@RequestBody UserCreateDTO userDTO接收表单数据
//2. 将请求数据转换为业务对象 - 例如，用户等待系统处理注册请求（此时用户看到"处理中"图标），
//   系统在后台将DTO对象转换为User实体类对象，在UserService中
//3. 将业务对象转换为响应数据 - 例如，将实体类对象转回为DTO对象，在UserController中
//4. 将响应数据返回给客户端 - 例如，将DTO对象转换为JSON格式返回给客户端，UserController做的

import com.neu.nuboard.model.Location;
import com.neu.nuboard.model.College;

public class UserCreateDTO {
    // 用于接收请求的字段
    private String username;
    private String email;
    private String program;   
    private Location location;
    private College college;
    // 用于响应的字段，在UserController中返回给客户端   
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

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public College getCollege() { return college; }
    public void setCollege(College college) { this.college = college; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

    public int getEventsCount() { return eventsCount; }
    public void setEventsCount(int eventsCount) { this.eventsCount = eventsCount; }
}                       
