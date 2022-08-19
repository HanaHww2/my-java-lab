package me.study.userservice.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.study.userservice.exception.DuplicateUserMailException;
import me.study.userservice.user.domain.RoleType;
import me.study.userservice.user.domain.User;
import me.study.userservice.user.dto.UserDto;
import me.study.userservice.user.repository.UserRepository;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(UserDto userDto) throws DuplicateUserMailException {
        if (userRepository.findByEmail(userDto.getEmail())
                .orElse(null) != null) {
            throw new DuplicateUserMailException("Already Exists User Mail");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .role(RoleType.USER)
                .build();

        log.info("{}", user);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

}
