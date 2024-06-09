package ru.practicum.ewm.controller.events;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.ewm.model.events.dto.EventFullDto;
import ru.practicum.ewm.model.events.dto.EventShortDto;

/**
 * Публичный API для работы с событиями
 */
@Validated
@RestController
@RequestMapping("/events")
public interface EventsPublicController {

    /**
     * Получение событий с возможностью фильтрации
     * это публичный эндпоинт, соответственно в выдаче должны быть только
     * опубликованные события
     * текстовый поиск (по аннотации и подробному описанию) должен быть без учета
     * регистра букв
     * если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно
     * выгружать события, которые произойдут позже текущей даты и времени
     * информация о каждом событии должна включать в себя количество просмотров и
     * количество уже одобренных заявок на участие
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает
     * пустой список
     *
     * @param text          текст для поиска в содержимом аннотации и подробном
     *                      описании события
     * @param categories    список идентификаторов категорий в которых будет вестись
     *                      поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на
     *                      участие
     * @param rangeStart    дата и время не раньше которых должно произойти событие
     * @param rangeEnd      дата и время не позже которых должно произойти событие
     * @param sort          Вариант сортировки: по дате события или по количеству
     *                      просмотров Available values : EVENT_DATE, VIEWS
     * @param from          количество событий, которые нужно пропустить для
     *                      формирования текущего набора
     * @param size          количество событий в наборе
     * @return 200 - События найдены List<EventShortDto>
     *         404 - Запрос составлен некорректно ApiError
     */
    @GetMapping()
    List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request);

    /**
     * событие должно быть опубликовано
     * информация о событии должна включать в себя количество просмотров и
     * количество подтвержденных запросов
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     *
     * @param eventId id события
     * @return 200 - Событие найдено EventFullDto
     *         400 - Запрос составлен некорректно ApiError
     *         404 - Запрос составлен некорректно ApiError
     */
    @GetMapping("{eventId}")
    EventFullDto getEvent(@PathVariable Long eventId,
            HttpServletRequest request);
}
