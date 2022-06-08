package com.bankIsland.user.service;

import com.bankIsland.user.controller.payload.request.SignupRequest;
import com.bankIsland.user.dao.RoleRepository;
import com.bankIsland.user.dao.UserRepository;
import com.bankIsland.user.dto.UserDto;
import com.bankIsland.user.entity.ERole;
import com.bankIsland.user.entity.Role;
import com.bankIsland.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    PasswordEncoder encoder;
    RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        userRepository.save(user);
        userDto.setId(user.getId());
        return userDto;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).get();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDto registration(SignupRequest signupRequest, int id) {
        UserDto userDto = new UserDto(signupRequest.email(),
                encoder.encode(signupRequest.password()),
                new HashSet<Role>(List.of(roleRepository.findByName(ERole.C).get())),
                id);
        return save(userDto);
    }

    @Override
    public boolean delete(UserDto userDto) {
        try {
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//    }
}









