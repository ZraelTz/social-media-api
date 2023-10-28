package com.socialmedia.api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity<Long> {

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;
}
