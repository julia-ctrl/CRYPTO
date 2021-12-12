package org.julia.lab56.service;

import org.julia.lab56.dto.UserDto;
import org.julia.lab56.model.User;

public interface UserService {
    void save(UserDto user);

    UserDto findByUsername(String username);

    UserDto userToDto(User user);
}
