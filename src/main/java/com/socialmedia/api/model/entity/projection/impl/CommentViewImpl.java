package com.socialmedia.api.model.entity.projection.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmedia.api.model.entity.Comment;
import com.socialmedia.api.model.entity.projection.CommentView;
import lombok.*;

import java.io.Serializable;

/**
 * A Projection for the {@link Comment} entity
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentViewImpl extends BaseViewImpl implements CommentView {

    @JsonProperty("comment")
    private String content;

    private int likeCount;

    private UserViewImpl user;
}