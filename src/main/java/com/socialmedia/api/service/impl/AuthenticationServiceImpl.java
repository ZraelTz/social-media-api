package com.socialmedia.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.api.dto.request.LoginRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.LoginResponse;
import com.socialmedia.api.exception.ApiException;
import com.socialmedia.api.exception.NotFoundException;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.repository.UserRepository;
import com.socialmedia.api.service.AuthenticationService;
import com.socialmedia.api.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            LoginResponse.LoggedInUserView loggedInUser = LoginResponse.LoggedInUserView.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .profilePicUrl(user.getProfilePicUrl())
                    .build();

            LoginResponse loginResponse = LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(loggedInUser)
                    .build();

            return ApiResponse.<LoginResponse>builder()
                    .data(loginResponse)
                    .statusMessage("Login successful")
                    .build();
        }

        throw new ApiException("Login failed");
    }


    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (StringUtils.isNotBlank(authHeader)) {
            User user = this.userRepository.findByEmailOrUsername(username, username)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                LoginResponse.LoggedInUserView loggedInUser = LoginResponse.LoggedInUserView.builder()
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .profilePicUrl(user.getProfilePicUrl())
                        .build();

                LoginResponse loginResponse = LoginResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .user(loggedInUser)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), loginResponse);
            }
        }
    }

}
