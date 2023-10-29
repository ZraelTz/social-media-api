package com.socialmedia.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post extends BaseEntity<Long> {

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false)
    @JsonIgnore
    private User user;

    @Builder.Default
    @Column(name = "like_count")
    private int likeCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private Set<PostLike> likes = new LinkedHashSet<>();

    public void updateLikeCount() {
        likeCount += 1;
    }

    public String getBy() {
        return user.getUsername();
    }

}
