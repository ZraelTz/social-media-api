package com.socialmedia.api.service;

import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.projection.PostView;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface PostService {

    @Transactional
    ApiResponse<Post> create(PostCreateRequest request);

    @Transactional
    ApiResponse<Post> update(PostUpdateRequest request);

    @Transactional(readOnly = true)
    ApiResponse<Page<PostView>> getAll(int page, int pageSize);

    @Transactional(readOnly = true)
    ApiResponse<Page<PostView>> getByUser(int page, int pageSize);

    @Transactional(readOnly = true)
    ApiResponse<PostView> getById(long postId);

    @Transactional
    ApiResponse<Post> delete(long postId);
}
