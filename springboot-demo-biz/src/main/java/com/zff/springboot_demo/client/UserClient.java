package com.zff.springboot_demo.client;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.client.dto.UserDTO;
import com.zff.springboot_demo.client.dto.RoleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", url = "${app.user-service.url:http://localhost:8081}")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    Result<UserDTO> getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users/{id}/roles")
    Result<List<RoleDTO>> getUserRoles(@PathVariable("id") Long id);

    @GetMapping("/api/users/{id}/isAdmin")
    Result<Boolean> isAdmin(@PathVariable("id") Long id);
}
