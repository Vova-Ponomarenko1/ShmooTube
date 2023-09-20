package org.example.Users;

import org.springframework.web.multipart.MultipartFile;

public class User {
    private Long id;
    private String username;
    private String password;

    private String email;
    private String role;

    private MultipartFile avatar;

    private String avatar_Base64;

    public String getAvatar_Base64() {
        return avatar_Base64;
    }

    public void setAvatar_Base64(String avatar_Base64) {
        this.avatar_Base64 = avatar_Base64;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
