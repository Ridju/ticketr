package com.ridju.backend.domain.dto;

public class CreateUserDTO {
    private String username;
    private String email;
    private String password;
    private String checkpassword;

    public CreateUserDTO() {}

    public CreateUserDTO(String username, String email, String password, String checkpassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.checkpassword = checkpassword;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckpassword() {
        return checkpassword;
    }

    public void setCheckpassword(String checkpassword) {
        this.checkpassword = checkpassword;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", checkpassword='" + checkpassword + '\'' +
                '}';
    }
}
