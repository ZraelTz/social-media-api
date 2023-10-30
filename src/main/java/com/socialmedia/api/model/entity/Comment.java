package com.socialmedia.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity implements Likable {

    @JsonProperty("comment")
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_fk", nullable = false)
    @JsonIgnore
    private Post post;

    @Builder.Default
    @Column(name = "like_count")
    private int likeCount = 0;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private Set<CommentLike> likes = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false)
    @JsonIgnore
    private User user;

    public boolean addLike(CommentLike commentLike) {
        if (Objects.nonNull(commentLike)) {
            if (likes.stream()
                    .anyMatch(like -> like.getUser().equals(commentLike.getUser()))) {
                return false;
            }

            boolean likeAdded = this.likes.add(commentLike);
            if (likeAdded) {
                likeCount += 1;
            }

            return likeAdded;
        }

        return false;
    }

    public boolean removeLike(CommentLike commentLike) {
        if (Objects.nonNull(commentLike)) {
            if (likes.stream().noneMatch(like -> like.getUser().equals(commentLike.getUser()))) {
                return false;
            }

            boolean removed = likes.remove(commentLike);
            if (removed) {
                likeCount -= 1;
            }

            return removed;
        }

        return false;
    }
}
