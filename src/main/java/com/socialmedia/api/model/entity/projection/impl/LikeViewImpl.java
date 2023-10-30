package com.socialmedia.api.model.entity.projection.impl;

import com.socialmedia.api.model.entity.PostLike;
import com.socialmedia.api.model.entity.projection.LikeView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A Projection for the {@link PostLike and @link CommentLike} entity
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LikeViewImpl extends BaseViewImpl implements LikeView {

    private UserViewImpl user;
}