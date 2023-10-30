package com.socialmedia.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post extends BaseEntity implements Likable {

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false)
    @JsonIgnore
    private User user;

    @Builder.Default
    @Column(name = "like_count")
    private int likeCount = 0;

    @Builder.Default
    @Column(name = "comment_count")
    private int commentCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private Set<PostLike> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public void updateLikeCount() {
        likeCount += 1;
    }

    public String getPostedBy() {
        return user.getUsername();
    }

    public void addComment(Comment comment) {
        if (Objects.nonNull(comment)) {
            this.comments.add(comment);
            commentCount += 1;
        }
    }

    public boolean removeComment(Comment comment) {
        if (Objects.nonNull(comment)) {
            boolean removed = this.comments.remove(comment);
            if (removed) {
                commentCount -= 1;
            }

            return removed;
        }

        return false;
    }

    public boolean addLike(PostLike postLike) {
        if (Objects.nonNull(postLike)) {
            if (likes.stream()
                    .anyMatch(like -> like.getUser().equals(postLike.getUser()))) {
                return false;
            }

            boolean likeAdded = this.likes.add(postLike);
            if (likeAdded) {
                likeCount += 1;
            }

            return likeAdded;
        }

        return false;
    }

    public boolean removeLike(PostLike postLike) {
        if (Objects.nonNull(postLike)) {
            if (likes.stream().noneMatch(like -> like.getUser().equals(postLike.getUser()))) {
                return false;
            }

            boolean removed = this.likes.remove(postLike);
            if (removed) {
                likeCount -= 1;
            }

            return removed;
        }

        return false;
    }

}
