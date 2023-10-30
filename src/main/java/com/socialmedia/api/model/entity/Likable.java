package com.socialmedia.api.model.entity;

import java.util.Collection;

public interface Likable {

    Collection<? extends Like> getLikes();
    int getLikeCount();
}
