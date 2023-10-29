package com.socialmedia.api.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DefaultMapper {

    default String mapEmptyString(String param) {
        return StringUtils.isNotBlank(param) ? StringUtils.normalizeSpace(param).trim() : null;
    }

    default Set<String> mapEmptySet(Set<String> params) {
        return params != null && !params.isEmpty() ? params.stream()
                .map(p->StringUtils.normalizeSpace(p).trim())
                .collect(Collectors.toSet()) : null;
    }
}
