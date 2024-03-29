package com.socialmedia.api.model.entity.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmedia.api.model.entity.Post;

/**
 * A Projection for the {@link Post} entity
 */
public interface PostView extends BaseView {

    String getContent();

    int getLikeCount();

    int getCommentCount();

    @JsonProperty("postedBy")
    UserView getUser();

}