package ru.practicum.ewm.controller.requests;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.ewm.model.requests.dto.ParticipationRequestDto;

/**
 * Закрытый API для работы с запросами текущего пользователя на участие в
 * событиях
 */
@Validated
@RestController
@RequestMapping("users/{userId}/requests")
public interface RequestsPrivateController {

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает
     * пустой список
     *
     * @param userId id текущего пользователя
     * @return 200 - Найдены запросы на участие List<ParticipationRequestDto>
     *         400 - Запрос составлен некорректно ApiError
     *         404 - Пользователь не найден ApiError
     */
    @GetMapping()
    List<ParticipationRequestDto> getRequest(@PathVariable Long userId);

    /**
     * нельзя добавить повторный запрос (Ожидается код ошибки 409)
     * инициатор события не может добавить запрос на участие в своём событии
     * (Ожидается код ошибки 409)
     * нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
     * если у события достигнут лимит запросов на участие - необходимо вернуть
     * ошибку (Ожидается код ошибки 409)
     * если для события отключена пре-модерация запросов на участие, то запрос
     * должен автоматически перейти в состояние подтвержденного
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return 201 - Заявка создана ParticipationRequestDto
     *         400 - Запрос составлен некорректно ApiError
     *         404 - Событие не найдено или недоступно ApiError
     *         409 - Нарушение целостности данных ApiError
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto setRequest(
            @Positive @RequestParam Long eventId,
            @Positive @PathVariable Long userId);

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId    id текущего пользователя
     * @param requestId id запроса на участие
     * @return 200 - Заявка отменена ParticipationRequestDto
     *         404 - Запрос не найден или недоступен ApiError
     */
    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto updateRequest(@PathVariable Long userId, @PathVariable Long requestId);
}
