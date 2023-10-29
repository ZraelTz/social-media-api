package com.socialmedia.api.service.impl;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.exception.ApiException;
import com.socialmedia.api.exception.NotFoundException;
import com.socialmedia.api.mapper.UserMapper;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.repository.UserRepository;
import com.socialmedia.api.service.AuthenticationService;
import com.socialmedia.api.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authService;

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

        User user = userMapper.createUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        RegistrationResponse registrationResponse = RegistrationResponse.builder()
                .username(username)
                .email(email)
                .build();

        return ApiResponse.<RegistrationResponse>builder()
                .data(registrationResponse)
                .message("Registration successful")
                .build();
    }

    @Override
    public ApiResponse<User> follow(String username) {
        if (StringUtils.isBlank(username)) {
            throw new ApiException("Username is blank");
        }

        User authUser = authService.getAuthUser();
        Optional<User> optionalUser = userRepository.findByEmailOrUsername(username, username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User userToBeFollowed = optionalUser.get();
        boolean followingAdded = authUser.addFollowing(userToBeFollowed);
        if (!followingAdded) {
            throw new ApiException("You're already following this user");
        }

        userRepository.save(authUser);
        userToBeFollowed.addFollower(authUser);
        userRepository.save(userToBeFollowed);

        return ApiResponse.<User>builder()
                .data(userToBeFollowed)
                .message("User followed")
                .build();
    }

    @Override
    public ApiResponse<User> unfollow(String username) {
        if (StringUtils.isBlank(username)) {
            throw new ApiException("Username is blank");
        }

        User authUser = authService.getAuthUser();
        Optional<User> optionalUser = userRepository.findByEmailOrUsername(username, username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User userToBeUnfollowed = optionalUser.get();
        boolean followingRemoved = authUser.removeFollowing(userToBeUnfollowed);
        if (!followingRemoved) {
            throw new ApiException("You're not following this user");
        }

        userRepository.save(authUser);
        userToBeUnfollowed.removeFollower(authUser);
        userRepository.save(userToBeUnfollowed);

        return ApiResponse.<User>builder()
                .data(userToBeUnfollowed)
                .message("User unfollowed")
                .build();
    }


}
