package com.bankIsland.user.service;

import com.bankIsland.user.dao.UserRepository;
import com.bankIsland.user.dto.UserDto;
import com.bankIsland.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void delete(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        userRepository.delete(user);
    }

    //    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//    }
}









