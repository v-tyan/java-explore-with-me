package ru.practicum.ewm.controller.events;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.events.State;
import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.service.events.EventsAdminService;

/**
 * API для работы с событиями
 */
@Validated
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventsAdminController {
    private final EventsAdminService service;

    /**
     * Поиск событий
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих под
     * переданные условия
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает
     * пустой список
     *
     * @param users      список id пользователей, чьи события нужно найти
     * @param categories список id категорий в которых будет вестись поиск
     * @param states     список состояний в которых находятся искомые события
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd   дата и время не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для
     *                   формирования текущего набора
     * @param size       количество событий в наборе
     * @return 200 - События найдены List<EventFullDto>
     *         400 - Запрос составлен некорректно ApiError
     */
    @GetMapping()
    List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        return service.getEvents(users, categories, states, rangeStart, rangeEnd, from, size);
    }

    /**
     * Редактирование данных события и его статуса (отклонение/публикация).
     * Редактирование данных любого события администратором. Валидация данных не
     * требуется.
     * дата начала изменяемого события должна быть не ранее чем за час от даты
     * публикации. (Ожидается код ошибки 409)
     * событие можно публиковать, только если оно в состоянии ожидания публикации
     * (Ожидается код ошибки 409)
     * событие можно отклонить, только если оно еще не опубликовано (Ожидается код
     * ошибки 409)
     *
     * @param updateEventAdminRequest Данные для изменения информации о событии
     * @param eventId                 id события
     * @return 200 - Событие отредактировано EventFullDto
     *         404 - Событие не найдено или недоступно ApiError
     *         409 - Событие не удовлетворяет правилам редактирования ApiError
     */

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{eventId}")
    EventFullDto updateEvent(
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
            @PathVariable Long eventId) {
        return service.updateEvent(updateEventAdminRequest, eventId);
    }
}
