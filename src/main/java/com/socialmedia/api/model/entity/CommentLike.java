package com.socialmedia.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment_like")
public class CommentLike extends Like {
    @ManyToOne
    @JoinColumn(name = "comment_fk", nullable = false)
    private Comment comment;
}
