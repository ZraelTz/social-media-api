package com.socialmedia.api.service;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.request.update.UserFollowingUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.UserView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    ApiResponse<RegistrationResponse> registerUser(UserRegistrationRequest request);

    @Transactional
    ApiResponse<User> updateFollowing(UserFollowingUpdateRequest request);

    @Transactional(readOnly = true)
    ApiResponse<RestPage<UserView>> getFollowings(int page, int pageSize);

    @Transactional(readOnly = true)
    ApiResponse<RestPage<UserView>> getFollowers(int page, int pageSize);

}
