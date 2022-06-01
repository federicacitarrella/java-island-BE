package com.bankIsland.user.dto;

import com.bankIsland.user.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

public class UserDto {

    private int id;

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    private int accountOwnerId;

    public UserDto() {
    }

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDto(String username, String password, Set<Role> roles, int userId) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.accountOwnerId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(int userId) {
        this.accountOwnerId = userId;
    }
}
