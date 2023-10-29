package com.socialmedia.api.controller;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@RequestBody @Valid
                                                                      UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

}
