package com.bankIsland.user.service;

import com.bankIsland.user.dto.UserDto;

public interface UserService {

    public UserDto save(UserDto userDto);

    public boolean existsByUsername(String username);

    public UserDto findByUsername(String username);

    public void deleteById(int id);

    public void delete(UserDto userDto);

}
