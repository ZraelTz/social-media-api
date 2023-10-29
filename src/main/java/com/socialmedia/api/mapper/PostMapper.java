package com.socialmedia.api.mapper;

import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.model.entity.Post;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PostMapper extends DefaultMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Post createPost(PostCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePost(PostUpdateRequest request, @MappingTarget Post post);

}
