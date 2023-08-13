package ru.practicum.ewm.main.service.user.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.dto.UserSimpleDto;
import ru.practicum.ewm.main.service.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToDto(User user);

    User userFromDto(UserDto userDto);

    UserSimpleDto userToSimpleDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
