package com.socialmedia.api.model.entity.projection.impl;

import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.UserView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A Projection for the {@link User} entity
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserViewImpl extends BaseViewImpl implements UserView {

    private String profilePicUrl;

    private String username;
}