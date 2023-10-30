package com.socialmedia.api.service.impl;

import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.LikeUpdateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.core.exception.ApiException;
import com.socialmedia.api.core.exception.NotFoundException;
import com.socialmedia.api.mapper.PostMapper;
import com.socialmedia.api.model.entity.*;
import com.socialmedia.api.model.entity.projection.PostView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import com.socialmedia.api.repository.PostLikeRepository;
import com.socialmedia.api.repository.PostRepository;
import com.socialmedia.api.repository.UserRepository;
import com.socialmedia.api.service.AuthenticationService;
import com.socialmedia.api.service.PostService;
import com.socialmedia.api.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<Post> createPost(PostCreateRequest request) {
        User authUser = authService.getAuthUser();
        Post post = postMapper.createPost(request);
        post.setUser(authUser);
        Post persistedPost = postRepository.save(post);

        authUser.addPost(persistedPost);
        userRepository.save(authUser);
        return ApiResponse.<Post>builder()
                .data(post)
                .message("Post created")
                .build();
    }

    @Override
    public ApiResponse<Post> updatePost(PostUpdateRequest request) {
        User authUser = authService.getAuthUser();
        Optional<Post> optionalPost = postRepository.findByIdAndUser(request.getId(), authUser);
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
    public ApiResponse<Post> updateLike(LikeUpdateRequest request) {
        Long id = request.getId();
        if (id <= 0) {
            throw new ApiException("Invalid Comment Id");
        }

        Optional<Post> optionalComment = postRepository.findById(id);
        if (optionalComment.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }

        Post post = optionalComment.get();
        User authUser = authService.getAuthUser();
        Boolean isLike = request.getLike();
        if (isLike) {
            return like(authUser, post);
        }

        return unlike(authUser, post);
    }

    private ApiResponse<Post> like(User authUser, Post post) {
        PostLike postLike = PostLike.builder()
                .post(post)
                .user(authUser)
                .build();

        boolean likeAdded = post.addLike(postLike);
        if (!likeAdded) {
            return ApiResponse.<Post>builder()
                    .data(post)
                    .message("Post already liked")
                    .build();
        }

        postLikeRepository.save(postLike);
        postRepository.save(post);
        return ApiResponse.<Post>builder()
                .data(post)
                .message("Post liked")
                .build();
    }

    private ApiResponse<Post> unlike(User authUser, Post post) {
        Optional<PostLike> optionalPostLike = post.getLikes().stream()
                .filter(like -> like.getUser().equals(authUser))
                .findFirst();

        if (optionalPostLike.isEmpty()) {
            return ApiResponse.<Post>builder()
                    .data(post)
                    .message("Post already unliked")
                    .build();
        }

        PostLike postLike = optionalPostLike.get();
        boolean likeRemoved = post.removeLike(postLike);
        if (!likeRemoved) {
            return ApiResponse.<Post>builder()
                    .data(post)
                    .message("Post already unliked")
                    .build();
        }

        postRepository.save(post);
        postLikeRepository.delete(postLike);
        return ApiResponse.<Post>builder()
                .data(post)
                .message("Post unliked")
                .build();
    }

    @Override
    public ApiResponse<RestPage<PostView>> getAllPosts(int page, int pageSize) {
        RestPage<PostView> posts
                = new RestPage<>(postRepository.findPostsBy(PaginationUtil.pageable(Post.class, page, pageSize)));
        return ApiResponse.<RestPage<PostView>>builder()
                .data(posts)
                .build();
    }

    @Override
    public ApiResponse<RestPage<PostView>> getPostsByUser(String username, int page, int pageSize) {
        if (StringUtils.isBlank(username)) {
            throw new ApiException("Username is blank");
        }

        if (!userRepository.existsByUsername(username)) {
            throw new NotFoundException("Username not found");
        }

        RestPage<PostView> userPosts
                = new RestPage<>(postRepository.findByUser_Username(username, PaginationUtil.pageable(Post.class, page, pageSize)));
        return ApiResponse.<RestPage<PostView>>builder()
                .data(userPosts)
                .build();
    }

    @Override
    public ApiResponse<PostView> getPostById(long postId) {
        if (postId <= 0) {
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
    public ApiResponse<Post> deletePost(long postId) {
        if (postId <= 0) {
            throw new ApiException("Invalid Post Id");
        }

        User authUser = authService.getAuthUser();
        Optional<Post> optionalPost = postRepository.findByIdAndUser(postId, authUser);
        if (optionalPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }

        Post post = optionalPost.get();
        boolean postRemoved = authUser.removePost(post);
        if (postRemoved) {
            postRepository.delete(post);
            userRepository.save(authUser);
        }

        return ApiResponse.<Post>builder()
                .data(post)
                .message("Post deleted successfully")
                .build();
    }


}
