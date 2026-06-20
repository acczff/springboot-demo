package com.zff.springboot_demo.user.service;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.client.UserClient;
import com.zff.springboot_demo.client.dto.UserDTO;
import com.zff.springboot_demo.client.dto.RoleDTO;
import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.role.entity.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public User findById(Long id) {
        Result<UserDTO> result = userClient.getUserById(id);
        if (result == null || result.getCode() != 200 || result.getData() == null) {
            throw new RuntimeException("远程调用获取用户信息失败");
        }
        UserDTO dto = result.getData();
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        return user;
    }

    public boolean isAdmin(Long userId) {
        Result<Boolean> result = userClient.isAdmin(userId);
        if (result == null || result.getCode() != 200 || result.getData() == null) {
            throw new RuntimeException("远程调用获取管理员状态失败");
        }
        return result.getData();
    }

    public List<Role> getUserRoles(Long userId) {
        Result<List<RoleDTO>> result = userClient.getUserRoles(userId);
        if (result == null || result.getCode() != 200 || result.getData() == null) {
            throw new RuntimeException("远程调用获取用户角色失败");
        }
        return result.getData().stream().map(dto -> {
            Role role = new Role();
            role.setId(dto.getId());
            role.setName(dto.getName());
            role.setCode(dto.getCode());
            return role;
        }).collect(Collectors.toList());
    }
}
