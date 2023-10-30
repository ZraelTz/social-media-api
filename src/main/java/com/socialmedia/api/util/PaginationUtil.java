package com.socialmedia.api.util;

import com.socialmedia.api.model.entity.Likable;
import com.socialmedia.api.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@Slf4j
public class PaginationUtil {

    private PaginationUtil() {
    }

    public static <T> Pageable pageable(Class<T> entityClass, int page, int pageSize) {
        if (Arrays.asList(entityClass.getInterfaces()).contains(Likable.class)) {
            //sort by most likes
            return PageRequest.of(page, pageSize, Sort.Direction.DESC, "likeCount");
        }

        if (entityClass.equals(User.class)) {
            //sort by most followers
            return PageRequest.of(page, pageSize, Sort.Direction.DESC, "followerCount");
        }

        return PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
    }

    public static Pageable pageable(int page, int pageSize) {
        return PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
    }

}
