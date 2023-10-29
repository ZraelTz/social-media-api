package com.socialmedia.api.mapper;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.model.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper extends DefaultMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "password", target = "password", ignore = true)
    User toUser(UserRegistrationRequest request);
}
