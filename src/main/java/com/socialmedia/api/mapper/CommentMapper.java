package com.socialmedia.api.mapper;

import com.socialmedia.api.dto.request.CommentCreateRequest;
import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.dto.request.update.CommentUpdateRequest;
import com.socialmedia.api.dto.request.update.PostUpdateRequest;
import com.socialmedia.api.model.entity.Comment;
import com.socialmedia.api.model.entity.Post;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommentMapper extends DefaultMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment createComment(CommentCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(CommentUpdateRequest request, @MappingTarget Comment comment);

}
