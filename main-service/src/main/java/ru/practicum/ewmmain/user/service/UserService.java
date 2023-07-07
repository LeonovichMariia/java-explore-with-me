package ru.practicum.ewmmain.user.service;

import ru.practicum.ewmmain.user.dto.NewUserRequest;
import ru.practicum.ewmmain.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(Long[] ids, Integer size, Integer from);

    void deleteUserById(Long userId);
}
