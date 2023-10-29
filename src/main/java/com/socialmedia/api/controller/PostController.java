package com.socialmedia.api.controller;

import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.projection.PostView;
import com.socialmedia.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse<Post>> create(@RequestBody @Valid PostCreateRequest request) {
        return ResponseEntity.ok(postService.create(request));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Post>> update(@RequestBody @Valid PostUpdateRequest request) {
        return ResponseEntity.ok(postService.update(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostView>>> getAll(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(postService.getAll(page, pageSize));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<ApiResponse<Page<PostView>>> getByUser(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(postService.getByUser(page, pageSize));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<ApiResponse<PostView>> getById(@PathVariable long postId) {
        return ResponseEntity.ok(postService.getById(postId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Post>> delete(@PathVariable long postId) {
        return ResponseEntity.ok(postService.delete(postId));
    }


}
