package com.socialmedia.api.controller;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.request.update.UserFollowingUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.UserView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import com.socialmedia.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(description = "Register as a new user")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@RequestBody @Valid UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/followings")
    @Operation(description = "Fetch the users you are following as a logged in user")
    public ResponseEntity<ApiResponse<RestPage<UserView>>> getFollowings(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(userService.getFollowings(page, pageSize));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/followers")
    @Operation(description = "Fetch your followers")
    public ResponseEntity<ApiResponse<RestPage<UserView>>> getFollowers(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(userService.getFollowers(page, pageSize));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping
    @Operation(description = "Follow or Unfollow a User as a logged in user")
    public ResponseEntity<ApiResponse<User>> updateFollowing(@RequestBody @Valid UserFollowingUpdateRequest request) {
        return ResponseEntity.ok(userService.updateFollowing(request));
    }

}
