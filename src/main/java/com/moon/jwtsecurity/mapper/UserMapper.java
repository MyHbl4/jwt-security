package com.moon.jwtsecurity.mapper;

import com.moon.jwtsecurity.controller.dto.UserDTO;
import com.moon.jwtsecurity.controller.dto.UserNewDTO;
import com.moon.jwtsecurity.controller.dto.UserShortDTO;
import com.moon.jwtsecurity.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserShortDTO toUserShortDtoFromUser(User user);

    UserNewDTO toUserNewDtoFromUser(User user);

    UserDTO toUserDtoFromUser(User user);

    User toUserFromUserNewDto(UserNewDTO newUserDTO);

    User toUserFromUserShortDto(UserShortDTO shortUserDTO);

    User toUserFromUserDto(UserDTO userDTO);
}
