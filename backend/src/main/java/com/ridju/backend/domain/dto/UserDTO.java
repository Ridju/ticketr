package com.ridju.backend.domain.dto;

import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Role;

import java.util.Collection;

public class UserDTO {

    private long Id;
    private String username;
    private String email;
    private String role;

    public UserDTO() {}

    public UserDTO(MyUser user) {
        this.Id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRoles().stream().findFirst().get().getName();
    }

    public UserDTO(long id, String username, String email, String role) {
        Id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
