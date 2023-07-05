package ru.practicum.ewmmain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.user.dto.NewUserRequest;
import ru.practicum.ewmmain.user.dto.UserDto;
import ru.practicum.ewmmain.user.mapper.UserMapper;
import ru.practicum.ewmmain.user.model.User;
import ru.practicum.ewmmain.user.repository.UserRepository;
import ru.practicum.ewmmain.utils.PageSetup;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest newUserRequest) {
        User newUser = UserMapper.toUser(newUserRequest);
        UserDto savedUser = UserMapper.toUserDto(userRepository.save(newUser));
        log.info("Пользователь {} сохранен", savedUser);
        return savedUser;
    }

    @Override
    public List<UserDto> getUsers(Long[] ids, Integer size, Integer from) {
        log.info("Получение списка всех пользователей");
        if (ids == null) {
            return userRepository.findAll(new PageSetup(from, size)).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findUsersByIdIn(ids, new PageSetup(from, size)).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.validateUser(userId);
        userRepository.deleteById(userId);
        log.info("Пользователь с id {} удален", userId);
    }
}
