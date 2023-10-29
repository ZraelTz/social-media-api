package com.socialmedia.api.service;

import com.socialmedia.api.dto.request.LoginRequest;
import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.LoginResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface UserService {

    ApiResponse<RegistrationResponse> register(UserRegistrationRequest request);


}
