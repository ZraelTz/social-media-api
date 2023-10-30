package com.socialmedia.api.service;

import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.LikeUpdateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.projection.PostView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import org.springframework.transaction.annotation.Transactional;

public interface PostService {

    @Transactional
    ApiResponse<Post> createPost(PostCreateRequest request);

    @Transactional
    ApiResponse<Post> updatePost(PostUpdateRequest request);

    @Transactional
    ApiResponse<Post> updateLike(LikeUpdateRequest request);

    @Transactional(readOnly = true)
    ApiResponse<RestPage<PostView>> getAllPosts(int page, int pageSize);

    @Transactional(readOnly = true)
    ApiResponse<RestPage<PostView>> getPostsByUser(String username, int page, int pageSize);

    @Transactional(readOnly = true)
    ApiResponse<PostView> getPostById(long postId);

    @Transactional
    ApiResponse<Post> deletePost(long postId);
}
