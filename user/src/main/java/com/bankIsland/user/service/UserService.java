package com.bankIsland.user.service;

import com.bankIsland.user.controller.payload.request.SignupRequest;
import com.bankIsland.user.dto.UserDto;

public interface UserService {

    public UserDto save(UserDto userDto);

    public boolean existsByUsername(String username);

    public UserDto findByUsername(String username);

    public UserDto registration(SignupRequest signupRequest, int id);

}
