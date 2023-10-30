package com.socialmedia.api.model.entity.projection;

import com.socialmedia.api.model.entity.PostLike;

/**
 * A Projection for the {@link PostLike and @link CommentLike} entity
 */
public interface LikeView extends BaseView {
    UserView getUser();
}