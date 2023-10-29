package com.socialmedia.api.service;

import com.socialmedia.api.dto.request.LoginRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    ApiResponse<LoginResponse> login(LoginRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
