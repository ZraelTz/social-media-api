package com.socialmedia.api.controller;

import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.LikeUpdateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.projection.PostView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import com.socialmedia.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody @Valid PostCreateRequest request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<ApiResponse<Post>> updatePost(@RequestBody @Valid PostUpdateRequest request) {
        return ResponseEntity.ok(postService.updatePost(request));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping
    public ResponseEntity<ApiResponse<Post>> updateLike(@RequestBody @Valid LikeUpdateRequest request) {
        return ResponseEntity.ok(postService.updateLike(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<RestPage<PostView>>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(postService.getAllPosts(page, pageSize));
    }

    @GetMapping(value = "/by-user/{username}")
    public ResponseEntity<ApiResponse<RestPage<PostView>>> getPostsByUser(@PathVariable String username,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(postService.getPostsByUser(username, page, pageSize));
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<ApiResponse<PostView>> getPostById(@PathVariable long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<ApiResponse<Post>> deletePost(@PathVariable long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }


}
