package com.socialmedia.api.model.entity.projection;

import com.socialmedia.api.model.entity.PostLike;

/**
 * A Projection for the {@link PostLike} entity
 */
public interface PostLikeView extends BaseView {
    UserView getUser();
}