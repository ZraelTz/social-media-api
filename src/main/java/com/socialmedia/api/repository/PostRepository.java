package com.socialmedia.api.repository;

import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.PostView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<PostView> findByUser(User user, Pageable pageable);

    Page<PostView> findPostsBy(Pageable pageable);

    PostView findPostById(long id);

    Optional<Post> findByIdAndUser(long postId, User user);
}