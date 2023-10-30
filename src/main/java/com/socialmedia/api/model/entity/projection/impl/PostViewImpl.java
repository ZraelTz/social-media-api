package com.socialmedia.api.model.entity.projection.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmedia.api.model.entity.Post;
import com.socialmedia.api.model.entity.projection.PostView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A Projection for the {@link Post} entity
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostViewImpl extends BaseViewImpl implements PostView {

    private String content;

    private int likeCount;

    private int commentCount;

    @JsonProperty("postedBy")
    private UserViewImpl user;

}