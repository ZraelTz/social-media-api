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
@Table(name = "post_like")
public class PostLike extends Like {

    @ManyToOne
    @JoinColumn(name = "post_fk", nullable = false)
    private Post post;
}
