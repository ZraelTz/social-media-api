package com.socialmedia.api.repository;

import com.socialmedia.api.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}