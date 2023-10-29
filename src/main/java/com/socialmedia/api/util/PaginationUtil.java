package com.socialmedia.api.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

    private PaginationUtil() {
    }

    public static Pageable pageable(int page, int pageSize) {
        return PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
    }

    public static Pageable pageable(int page, int pageSize, boolean sortByIdDesc) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return sortByIdDesc ? pageRequest.withSort(Sort.Direction.DESC, "id") : pageRequest;
    }
}
