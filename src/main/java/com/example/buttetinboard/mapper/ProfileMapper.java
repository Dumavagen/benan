package com.example.buttetinboard.mapper;

import com.example.buttetinboard.dto.ProfileResponse;
import com.example.buttetinboard.model.Profile;
import com.example.buttetinboard.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "id", source = "profile.id")
    @Mapping(target = "phoneNumber", source = "profile.phoneNumber")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    ProfileResponse mapToDTO(Profile profile, User user);

}
