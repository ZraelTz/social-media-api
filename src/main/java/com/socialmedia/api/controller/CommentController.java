package com.socialmedia.api.controller;

import com.socialmedia.api.dto.request.CommentCreateRequest;
import com.socialmedia.api.dto.request.update.CommentUpdateRequest;
import com.socialmedia.api.dto.request.update.LikeUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.model.entity.Comment;
import com.socialmedia.api.model.entity.projection.CommentView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import com.socialmedia.api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<Comment>> createComment(@RequestBody @Valid CommentCreateRequest request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<ApiResponse<Comment>> updateComment(@RequestBody @Valid CommentUpdateRequest request) {
        return ResponseEntity.ok(commentService.updateComment(request));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping
    public ResponseEntity<ApiResponse<Comment>> updateLike(@RequestBody @Valid LikeUpdateRequest request) {
        return ResponseEntity.ok(commentService.updateLike(request));
    }

    @GetMapping(value = "/by-post/{postId}")
    public ResponseEntity<ApiResponse<RestPage<CommentView>>> getCommentsByPost(@PathVariable long postId,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, page, pageSize));
    }

    @GetMapping(value = "/by-post-and-user/{postId}/{username}")
    public ResponseEntity<ApiResponse<RestPage<CommentView>>> getCommentsByUserAndPost(@PathVariable long postId,
                                                                                       @PathVariable String username,
                                                                                       @RequestParam(defaultValue = "0") int page,
                                                                                       @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(commentService.getCommentsByPostAndUser(postId, username, page, pageSize));
    }

    @GetMapping(value = "/{commentId}")
    public ResponseEntity<ApiResponse<CommentView>> getCommentById(@PathVariable long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<ApiResponse<Comment>> deleteComment(@PathVariable long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }


}
