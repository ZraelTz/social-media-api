package com.socialmedia.api.mapper;

import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.UserView;
import io.micrometer.common.util.StringUtils;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper extends DefaultMapper {


    @Named("toLowercase")
    static String toLowercase(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }

        return param.toLowerCase();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", qualifiedByName = "toLowercase")
    @Mapping(target = "username", qualifiedByName = "toLowercase")
    @Mapping(source = "password", target = "password", ignore = true)
    User createUser(UserRegistrationRequest request);

    default ArrayList<UserView> toUserViews(Collection<User> users) {
        return users.stream().map(user -> new UserView() {

            @Override
            public String getProfilePicUrl() {
                return user.getProfilePicUrl();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public Long getId() {
                return user.getId();
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return user.getCreatedAt();
            }

        }).collect(Collectors.toCollection(ArrayList::new));
    }


}
