package com.zff.springboot_demo.client.dto;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private List<RoleDTO> roles;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<RoleDTO> getRoles() { return roles; }
    public void setRoles(List<RoleDTO> roles) { this.roles = roles; }
}
