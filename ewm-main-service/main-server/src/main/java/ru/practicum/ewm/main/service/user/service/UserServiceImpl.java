package ru.practicum.ewm.main.service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.service.exception.ConflictException;
import ru.practicum.ewm.main.service.exception.EntityNotFoundException;
import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.mapper.UserMapper;
import ru.practicum.ewm.main.service.user.model.User;
import ru.practicum.ewm.main.service.user.repository.UserRepository;
import ru.practicum.ewm.main.service.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        try {
            User user = userMapper.userFromDto(userDto);
            User userSaved = userRepository.save(user);
            return userMapper.userToDto(userSaved);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("User with specified email is already exist.");
        }
    }

    @Override
    public List<UserDto> getAllUsers(Long[] ids, Integer from, Integer size) {
        Pagination page = new Pagination(from, size);

        if (ids == null) {
            return userRepository.findAll(page)
                    .stream()
                    .map(userMapper::userToDto)
                    .collect(Collectors.toList());
        }

        return userRepository.findAllByIdIn(ids, page)
                .stream()
                .map(userMapper::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        getOrThrow(userId);
        userRepository.deleteById(userId);
    }

    public User getOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, String.format("ID: %s", userId)));
    }
}
