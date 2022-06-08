package com.bankIsland.user.service;

import com.bankIsland.user.controller.payload.request.SignupRequest;
import com.bankIsland.user.dto.UserDto;

public interface UserService {

    UserDto save(UserDto userDto);

    boolean delete(UserDto userDto);

    boolean existsByUsername(String username);

    UserDto findByUsername(String username);

    UserDto registration(SignupRequest signupRequest, int id);

}
