package com.bankIsland.user.controller.payload.response;

import com.bankIsland.user.dto.UserDto;

public class EmployeeLoginResponse {

    private String token;
    private UserDto userDto;
    private String role;

    public EmployeeLoginResponse() {
    }

    public EmployeeLoginResponse(String token, UserDto userDto, String role) {
        this.token = token;
        this.userDto = userDto;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
