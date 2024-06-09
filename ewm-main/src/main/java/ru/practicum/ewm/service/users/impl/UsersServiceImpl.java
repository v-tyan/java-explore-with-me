package ru.practicum.ewm.service.users.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.errors.BadRequestException;
import ru.practicum.ewm.model.users.User;
import ru.practicum.ewm.model.users.UserMapper;
import ru.practicum.ewm.model.users.dto.NewUserRequest;
import ru.practicum.ewm.model.users.dto.UserDto;
import ru.practicum.ewm.repository.users.UsersRepository;
import ru.practicum.ewm.service.users.UsersService;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository repository;

    @Override
    public UserDto setUser(NewUserRequest newUserRequest) {
        User newUser = repository.save(UserMapper.toUser(newUserRequest));
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        return repository.findAllByIdIn(ids, pageable).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsers(Pageable pageable) {
        return repository.findAll(pageable).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        repository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
        repository.deleteById(userId);
    }
}
