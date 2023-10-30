package com.socialmedia.api.model.entity.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmedia.api.model.entity.Comment;

/**
 * A Projection for the {@link Comment} entity
 */
public interface CommentView extends BaseView {

    @JsonProperty("comment")
    String getContent();

    int getLikeCount();

    UserView getUser();
}