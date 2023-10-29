package com.socialmedia.api.service.impl;

import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.exception.ApiException;
import com.socialmedia.api.exception.NotFoundException;
import com.socialmedia.api.mapper.PostMapper;
import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.PostView;
import com.socialmedia.api.repository.PostRepository;
import com.socialmedia.api.service.AuthenticationService;
import com.socialmedia.api.service.PostService;
import com.socialmedia.api.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final AuthenticationService authService;

    @Override
    public ApiResponse<Post> create(PostCreateRequest request) {
        User authUser = authService.getAuthUser();
        Post post = postMapper.createPost(request);
        post.setUser(authUser);
        postRepository.save(post);

        return ApiResponse.<Post>builder()
                .data(post)
                .message("Post created")
                .build();
    }

    @Override
    public ApiResponse<Post> update(PostUpdateRequest request) {
        Optional<Post> optionalPost = postRepository.findById(request.getPostId());
        if (optionalPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }

        Post post = optionalPost.get();
        postMapper.updatePost(request, post);
        postRepository.save(post);

        return ApiResponse.<Post>builder()
                .data(post)
                .message("Post updated")
                .build();
    }

    @Override
    public ApiResponse<Page<PostView>> getAll(int page, int pageSize) {
        Page<PostView> posts = postRepository.findPostsBy(PaginationUtil.pageable(page, pageSize));
        return ApiResponse.<Page<PostView>>builder()
                .data(posts)
                .build();
    }

    @Override
    public ApiResponse<Page<PostView>> getByUser(int page, int pageSize) {
        User authUser = authService.getAuthUser();
        Page<PostView> postsByUser = postRepository.findByUser(authUser, PaginationUtil.pageable(page, pageSize));
        return ApiResponse.<Page<PostView>>builder()
                .data(postsByUser)
                .build();
    }

    @Override
    public ApiResponse<PostView> getById(long postId) {
        if (postId == 0) {
            throw new ApiException("Invalid Post Id");
        }

        PostView postView = postRepository.findPostById(postId);
        if (Objects.isNull(postView)) {
            throw new NotFoundException("Post not found");
        }

        return ApiResponse.<PostView>builder()
                .data(postView)
                .build();
    }

    @Override
    public ApiResponse<Post> delete(long postId) {
        User authUser = authService.getAuthUser();
        if (postId == 0) {
            throw new ApiException("Invalid Post Id");
        }

        Optional<Post> optionalPost = postRepository.findByIdAndUser(postId, authUser);
        if (optionalPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }

        Post post = optionalPost.get();
        postRepository.delete(post);
        return ApiResponse.<Post>builder()
                .data(post)
                .message("Post deleted successfully")
                .build();
    }


}
