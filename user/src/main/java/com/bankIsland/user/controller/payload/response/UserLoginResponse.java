package com.bankIsland.user.controller.payload.response;

import com.bankIsland.user.dto.AccountOwnerDto;

public class UserLoginResponse {

    private String token;
    private AccountOwnerDto accountOwnerDto;
    private String role;

    public UserLoginResponse() {
    }

    public UserLoginResponse(String token, AccountOwnerDto accountOwnerDto, String role) {
        this.token = token;
        this.accountOwnerDto = accountOwnerDto;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountOwnerDto getAccountOwnerDto() {
        return accountOwnerDto;
    }

    public void setAccountOwnerDto(AccountOwnerDto accountOwnerDto) {
        this.accountOwnerDto = accountOwnerDto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
