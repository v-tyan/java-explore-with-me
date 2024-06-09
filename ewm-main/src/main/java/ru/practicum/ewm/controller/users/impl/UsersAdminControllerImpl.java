package ru.practicum.ewm.controller.users.impl;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.controller.users.UsersAdminController;
import ru.practicum.ewm.model.users.dto.NewUserRequest;
import ru.practicum.ewm.model.users.dto.UserDto;
import ru.practicum.ewm.service.users.UsersService;

@RestController
@RequiredArgsConstructor
public class UsersAdminControllerImpl implements UsersAdminController {

    private final UsersService service;

    public UserDto setUser(NewUserRequest newUserRequest) {
        return service.setUser(newUserRequest);
    }

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        if (ids == null || ids.isEmpty()) {
            return service.getUsers(pageable);
        }
        return service.getUsers(ids, pageable);
    }

    public void deleteUser(Long userId) {
        service.deleteUser(userId);
    }
}
