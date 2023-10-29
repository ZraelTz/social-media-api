package com.socialmedia.api.service.impl;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.exception.ApiException;
import com.socialmedia.api.mapper.UserMapper;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.repository.UserRepository;
import com.socialmedia.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<RegistrationResponse> register(UserRegistrationRequest request) {
        String email = request.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new ApiException("Email already exists");
        }

        String username = request.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new ApiException("Username already exists");
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        RegistrationResponse registrationResponse = RegistrationResponse.builder()
                .username(username)
                .email(email)
                .build();

        return ApiResponse.<RegistrationResponse>builder()
                .data(registrationResponse)
                .statusMessage("Registration successful")
                .build();
    }


}
