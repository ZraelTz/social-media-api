package com.socialmedia.api.service;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    ApiResponse<RegistrationResponse> register(UserRegistrationRequest request);

    @Transactional
    ApiResponse<User> follow(String username);

    @Transactional
    ApiResponse<User> unfollow(String username);

}
