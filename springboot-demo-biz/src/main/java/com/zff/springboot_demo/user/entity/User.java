package com.zff.springboot_demo.user.entity;

import com.zff.springboot_demo.role.entity.Role;
import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String username;
    private String email;
    private List<Role> roles = new ArrayList<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }
}
