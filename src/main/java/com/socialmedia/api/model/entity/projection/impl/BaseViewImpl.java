package com.socialmedia.api.model.entity.projection.impl;

import com.socialmedia.api.model.entity.projection.BaseView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A Projection for the {@link com.socialmedia.api.model.entity.BaseEntity} entity
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseViewImpl implements BaseView {

    private Long id;

    private LocalDateTime createdAt;



}