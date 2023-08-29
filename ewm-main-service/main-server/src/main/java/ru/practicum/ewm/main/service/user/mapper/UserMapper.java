package ru.practicum.ewm.main.service.user.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.dto.UserShortDto;
import ru.practicum.ewm.main.service.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToDto(User user);

    User userFromDto(UserDto userDto);

    UserShortDto userToShortDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
