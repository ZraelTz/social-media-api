package com.socialmedia.api.model.entity.projection;

import java.time.LocalDateTime;

/**
 * A Projection for the {@link com.socialmedia.api.model.entity.BaseEntity} entity
 */
public interface BaseView {
    Long getId();

    LocalDateTime getCreatedAt();
}