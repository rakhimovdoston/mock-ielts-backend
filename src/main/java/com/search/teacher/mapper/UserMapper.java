package com.search.teacher.mapper;

import com.search.teacher.dto.message.UserResponse;
import com.search.teacher.model.entities.Role;
import com.search.teacher.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToRoleNames")
    UserResponse toResponse(User user);

    List<UserResponse> toList(List<User> users);

    @Named("mapRolesToRoleNames")
    static List<String> mapRolesToRoleNames(Set<Role> roles) {
        // Maps the set of Role to a list of role names
        return roles.stream()
                .map(Role::getName) // Extract the name from each Role
                .collect(Collectors.toList());
    }

}
