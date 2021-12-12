package org.julia.lab56.service;

import java.util.HashSet;
import org.apache.commons.lang3.RandomUtils;
import org.julia.lab56.dto.UserDto;
import org.julia.lab56.model.User;
import org.julia.lab56.repository.RoleRepository;
import org.julia.lab56.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${app.secret.text.password.prefix}")
    private String passwordPrefix;


    private String getPasswordForEncrypt(String userName) {
        return passwordPrefix + "_" + userName;
    }

    @Override
    public void save(UserDto userDto) {

        String salt = Long.toString(RandomUtils.nextLong(), 16);
        String passwordForEncrypt = getPasswordForEncrypt(userDto.getUsername());
        TextEncryptor encryptor = Encryptors.text(passwordForEncrypt, salt);
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPasswordConfirm()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        user.setSecretTextEncrypted(encryptor.encrypt(userDto.getSecretText()));
        user.setSalt(salt);
        userRepository.save(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return userToDto(user);

    }

    @Override
    public UserDto userToDto(User user) {
        if (null == user) {
            return null;
        }
        String salt = user.getSalt();
        String passwordForEncrypt = getPasswordForEncrypt(user.getUsername());
        TextEncryptor encryptor = Encryptors.text(passwordForEncrypt, salt);

        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setSecretText(encryptor.decrypt(user.getSecretTextEncrypted()));
        return dto;
    }
}
