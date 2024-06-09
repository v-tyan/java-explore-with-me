package ru.practicum.ewm.controller.users;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.ewm.model.users.dto.NewUserRequest;
import ru.practicum.ewm.model.users.dto.UserDto;

/**
 * API для работы с пользователями
 */

@Validated
@RestController
@RequestMapping("/admin/users")
public interface UsersAdminController {
    /**
     * Возвращает информацию обо всех пользователях (учитываются параметры
     * ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы)
     * В случае, если по заданным фильтрам не найдено ни одного пользователя,
     * возвращает пустой список
     *
     * @param ids  id пользователей
     * @param from количество элементов, которые нужно пропустить для формирования
     *             текущего набора
     * @param size количество элементов в наборе
     * @return 200 - Пользователи найдены List[UserDto]
     *         400 - Запрос составлен некорректно ApiError
     */
    @GetMapping()
    List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size);

    /**
     * @param newUserRequest Данные добавляемого пользователя
     * @return 201 - Пользователь зарегистрирован UserDto
     *         400 - Запрос составлен некорректно ApiError
     *         409 - Нарушение целостности данных ApiError
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto setUser(@RequestBody @Valid NewUserRequest newUserRequest);

    /**
     * @param userId id пользователя
     *               204 - Пользователь удален HttpStatus
     *               404 - Пользователь не найден или недоступен ApiError
     */
    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId);
}
