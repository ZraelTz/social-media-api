package com.socialmedia.api.controller;

import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.projection.PostView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import com.socialmedia.api.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(description = "Create post as an authenticated user")
    public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody @Valid PostCreateRequest request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    @Operation(description = "Update post as an authenticated user")
    public ResponseEntity<ApiResponse<Post>> updatePost(@RequestBody @Valid PostUpdateRequest request) {
        return ResponseEntity.ok(postService.updatePost(request));
    }

    @GetMapping
    @Operation(description = "Fetch all posts")
    public ResponseEntity<ApiResponse<RestPage<PostView>>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(postService.getAllPosts(page, pageSize));
    }

    @GetMapping(value = "/by-user/{username}")
    @Operation(description = "Fetch posts by user")
    public ResponseEntity<ApiResponse<RestPage<PostView>>> getPostsByUser(@PathVariable String username,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(postService.getPostsByUser(username, page, pageSize));
    }

    @GetMapping(value = "/{postId}")
    @Operation(description = "Fetch single post")
    public ResponseEntity<ApiResponse<PostView>> getPostById(@PathVariable long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/{postId}")
    @Operation(description = "Delete post as an authenticated user")
    public ResponseEntity<ApiResponse<Post>> deletePost(@PathVariable long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }


}
