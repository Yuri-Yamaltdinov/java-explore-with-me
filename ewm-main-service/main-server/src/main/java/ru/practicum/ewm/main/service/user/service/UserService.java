package ru.practicum.ewm.main.service.user.service;

import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    List<UserDto> getAllUsers(Long[] ids, Integer from, Integer size);

    void delete(Long userId);

    User getOrThrow(Long userId);
}
