package com.socialmedia.api.service.impl;

import com.socialmedia.api.core.exception.ApiException;
import com.socialmedia.api.core.exception.NotFoundException;
import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.request.update.UserFollowingUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.mapper.UserMapper;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.UserView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import com.socialmedia.api.repository.UserRepository;
import com.socialmedia.api.service.AuthenticationService;
import com.socialmedia.api.service.UserService;
import com.socialmedia.api.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authService;

    @Override
    public ApiResponse<RegistrationResponse> registerUser(UserRegistrationRequest request) {
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
    public ApiResponse<User> updateFollowing(UserFollowingUpdateRequest request) {
        String username = request.getUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Username not found");
        }

        Boolean isFollow = request.getFollow();
        User user = optionalUser.get();
        User authUser = authService.getAuthUser();
        if (isFollow) {
            return follow(authUser, user);
        }

        return unfollow(authUser, user);
    }

    @Override
    public ApiResponse<RestPage<UserView>> getFollowings(int page, int pageSize) {
        User authUser = authService.getAuthUser();
        ArrayList<UserView> followings = userMapper.toUserViews(authUser.getFollowings());
        RestPage<UserView> users
                = new RestPage<>(followings, PaginationUtil.pageable(User.class, page, pageSize), followings.size());

        return ApiResponse.<RestPage<UserView>>builder()
                .data(users)
                .build();
    }

    @Override
    public ApiResponse<RestPage<UserView>> getFollowers(int page, int pageSize) {
        User authUser = authService.getAuthUser();
        List<UserView> followers = userMapper.toUserViews(authUser.getFollowers());
        RestPage<UserView> users
                = new RestPage<>(followers, PaginationUtil.pageable(User.class, page, pageSize), followers.size());

        return ApiResponse.<RestPage<UserView>>builder()
                .data(users)
                .build();
    }

    private ApiResponse<User> follow(User authUser, User userToBeFollowed) {
        boolean followingAdded = authUser.addFollowing(userToBeFollowed);
        if (!followingAdded) {
            return ApiResponse.<User>builder()
                    .data(userToBeFollowed)
                    .message("User already followed")
                    .build();
        }

        userRepository.save(authUser);
        userToBeFollowed.addFollower(authUser);
        userRepository.save(userToBeFollowed);

        return ApiResponse.<User>builder()
                .data(userToBeFollowed)
                .message("User followed")
                .build();
    }

    private ApiResponse<User> unfollow(User authUser, User userToBeUnfollowed) {
        boolean followingRemoved = authUser.removeFollowing(userToBeUnfollowed);
        if (!followingRemoved) {
            return ApiResponse.<User>builder()
                    .data(userToBeUnfollowed)
                    .message("User already unfollowed")
                    .build();
        }

        userRepository.save(authUser);
        userToBeUnfollowed.removeFollower(authUser);
        userRepository.save(userToBeUnfollowed);

        return ApiResponse.<User>builder()
                .data(userToBeUnfollowed)
                .message("User unfollowed").build();
    }


}
