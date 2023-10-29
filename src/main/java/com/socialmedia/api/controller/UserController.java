package com.socialmedia.api.controller;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping(path = "/follow")
    public ResponseEntity<ApiResponse<User>> follow(@RequestParam String username) {
        return ResponseEntity.ok(userService.follow(username));
    }

    @PatchMapping(path = "/unfollow")
    public ResponseEntity<ApiResponse<User>> unfollow(@RequestParam String username) {
        return ResponseEntity.ok(userService.unfollow(username));
    }

}
