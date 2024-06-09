package ru.practicum.ewm.service.users;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm.model.users.dto.NewUserRequest;
import ru.practicum.ewm.model.users.dto.UserDto;

@Transactional(readOnly = true)
public interface UsersService {

    @Transactional
    UserDto setUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    List<UserDto> getUsers(Pageable pageable);

    void deleteUser(Long userId);
}
