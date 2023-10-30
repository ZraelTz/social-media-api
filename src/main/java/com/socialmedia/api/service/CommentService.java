package com.socialmedia.api.service;

import com.socialmedia.api.dto.request.CommentCreateRequest;
import com.socialmedia.api.dto.request.update.CommentUpdateRequest;
import com.socialmedia.api.dto.request.update.LikeUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.model.entity.Comment;
import com.socialmedia.api.model.entity.projection.CommentView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {

    @Transactional
    ApiResponse<Comment> createComment(CommentCreateRequest request);

    @Transactional
    ApiResponse<Comment> updateComment(CommentUpdateRequest request);

    @Transactional
    ApiResponse<Comment> updateLike(LikeUpdateRequest request);

    @Transactional(readOnly = true)
    ApiResponse<RestPage<CommentView>> getCommentsByPost(long postId, int page, int pageSize);

    @Transactional(readOnly = true)
    ApiResponse<RestPage<CommentView>> getCommentsByPostAndUser(long postId, String username, int page, int pageSize);

    @Transactional(readOnly = true)
    ApiResponse<CommentView> getCommentById(long commentId);

    @Transactional
    ApiResponse<Comment> deleteComment(long commentId);
}
