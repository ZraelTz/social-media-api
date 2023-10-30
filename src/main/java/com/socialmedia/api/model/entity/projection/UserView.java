package com.socialmedia.api.model.entity.projection;

import com.socialmedia.api.model.entity.User;

/**
 * A Projection for the {@link User} entity
 */
public interface UserView extends BaseView {

    String getProfilePicUrl();

    String getUsername();
}