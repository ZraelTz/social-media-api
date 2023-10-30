package com.socialmedia.api.repository;

import com.socialmedia.api.model.entity.Comment;
import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.CommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUser(Long id, User user);

    Page<CommentView> findByPost(Post post, Pageable pageable);

    Page<CommentView> findByPostAndUser(Post post, User user, Pageable pageable);

    CommentView findCommentById(long id);

    Page<CommentView> findByPostAndUser_Username(Post post, String username, Pageable pageable);
}