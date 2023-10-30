package com.socialmedia.api.service.impl;

import com.socialmedia.api.dto.request.CommentCreateRequest;
import com.socialmedia.api.dto.request.update.CommentUpdateRequest;
import com.socialmedia.api.dto.request.update.LikeUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.core.exception.ApiException;
import com.socialmedia.api.core.exception.NotFoundException;
import com.socialmedia.api.mapper.CommentMapper;
import com.socialmedia.api.model.entity.*;
import com.socialmedia.api.model.entity.projection.CommentView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import com.socialmedia.api.repository.CommentLikeRepository;
import com.socialmedia.api.repository.CommentRepository;
import com.socialmedia.api.repository.PostRepository;
import com.socialmedia.api.repository.UserRepository;
import com.socialmedia.api.service.AuthenticationService;
import com.socialmedia.api.service.CommentService;
import com.socialmedia.api.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthenticationService authService;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<Comment> createComment(CommentCreateRequest request) {
        Long postId = request.getPostId();
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }

        Post post = optionalPost.get();
        Comment comment = commentMapper.createComment(request);
        comment.setPost(post);
        comment.setUser(authService.getAuthUser());
        Comment persistedComment = commentRepository.save(comment);

        post.addComment(persistedComment);
        postRepository.save(post);

        return ApiResponse.<Comment>builder()
                .data(comment)
                .message("Comment created")
                .build();
    }

    @Override
    public ApiResponse<Comment> updateComment(CommentUpdateRequest request) {
        User authUser = authService.getAuthUser();
        Optional<Comment> optionalComment = commentRepository.findByIdAndUser(request.getId(), authUser);
        if (optionalComment.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }

        Comment comment = optionalComment.get();
        commentMapper.updateComment(request, comment);
        commentRepository.save(comment);

        return ApiResponse.<Comment>builder()
                .data(comment)
                .message("Comment updated")
                .build();
    }

    @Override
    public ApiResponse<Comment> updateLike(LikeUpdateRequest request) {
        Long id = request.getId();
        if (id <= 0) {
            throw new ApiException("Invalid Comment Id");
        }

        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }

        Comment comment = optionalComment.get();
        User authUser = authService.getAuthUser();
        Boolean isLike = request.getLike();
        if (isLike) {
            return like(authUser, comment);
        }

        return unlike(authUser, comment);
    }

    @Override
    public ApiResponse<RestPage<CommentView>> getCommentsByPost(long postId, int page, int pageSize) {
        if (postId <= 0) {
            throw new ApiException("Invalid Post Id");
        }

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }

        Post post = optionalPost.get();
        RestPage<CommentView> comments
                = new RestPage<>(commentRepository.findByPost(post, PaginationUtil.pageable(Comment.class, page, pageSize)));
        return ApiResponse.<RestPage<CommentView>>builder()
                .data(comments)
                .build();
    }

    @Override
    public ApiResponse<RestPage<CommentView>> getCommentsByPostAndUser(long postId, String username,
                                                                       int page, int pageSize) {
        if (StringUtils.isBlank(username)) {
            throw new ApiException("Username is blank");
        }

        if (postId <= 0) {
            throw new ApiException("Invalid Post Id");
        }

        if (!userRepository.existsByUsername(username)) {
            throw new NotFoundException("Username not found");
        }

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }

        Post post = optionalPost.get();
        RestPage<CommentView> commentsByUserOnPost
                = new RestPage<>(commentRepository.findByPostAndUser_Username(post, username, PaginationUtil.pageable(Comment.class, page, pageSize)));

        return ApiResponse.<RestPage<CommentView>>builder()
                .data(commentsByUserOnPost)
                .build();
    }

    private ApiResponse<Comment> like(User authUser, Comment comment) {
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .user(authUser)
                .build();

        boolean likeAdded = comment.addLike(commentLike);
        if (!likeAdded) {
            return ApiResponse.<Comment>builder()
                    .data(comment)
                    .message("Comment already liked")
                    .build();
        }

        commentLikeRepository.save(commentLike);
        commentRepository.save(comment);
        return ApiResponse.<Comment>builder()
                .data(comment)
                .message("Comment liked")
                .build();
    }

    private ApiResponse<Comment> unlike(User authUser, Comment comment) {
        Optional<CommentLike> optionalCommentLike = comment.getLikes().stream()
                .filter(like -> like.getUser().equals(authUser))
                .findFirst();

        if (optionalCommentLike.isEmpty()) {
            return ApiResponse.<Comment>builder()
                    .data(comment)
                    .message("Comment already unliked")
                    .build();
        }

        CommentLike commentLike = optionalCommentLike.get();
        boolean likeRemoved = comment.removeLike(commentLike);
        if (!likeRemoved) {
            return ApiResponse.<Comment>builder()
                    .data(comment)
                    .message("Comment already unliked")
                    .build();
        }

        commentRepository.save(comment);
        commentLikeRepository.delete(commentLike);
        return ApiResponse.<Comment>builder()
                .data(comment)
                .message("Comment unliked")
                .build();
    }

    @Override
    public ApiResponse<CommentView> getCommentById(long commentId) {
        if (commentId <= 0) {
            throw new ApiException("Invalid Comment Id");
        }

        CommentView commentView = commentRepository.findCommentById(commentId);
        if (Objects.isNull(commentView)) {
            throw new NotFoundException("Comment not found");
        }

        return ApiResponse.<CommentView>builder()
                .data(commentView)
                .build();
    }

    @Override
    public ApiResponse<Comment> deleteComment(long commentId) {
        if (commentId <= 0) {
            throw new ApiException("Invalid Comment Id");
        }

        User authUser = authService.getAuthUser();
        Optional<Comment> optionalComment = commentRepository.findByIdAndUser(commentId, authUser);
        if (optionalComment.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }

        Comment comment = optionalComment.get();
        Post post = comment.getPost();
        boolean commentRemoved = post.removeComment(comment);
        if (commentRemoved) {
            commentRepository.delete(comment);
            postRepository.save(post);
        }

        return ApiResponse.<Comment>builder()
                .data(comment)
                .message("Comment deleted successfully")
                .build();
    }


}
