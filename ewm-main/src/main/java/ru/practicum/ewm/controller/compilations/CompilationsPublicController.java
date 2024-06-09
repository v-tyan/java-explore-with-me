package ru.practicum.ewm.controller.compilations;

import java.util.List;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.ewm.model.compilations.dto.CompilationDto;

/**
 * Публичный API для работы с подборками событий
 */
@Validated
@RestController
@RequestMapping("/compilations")
public interface CompilationsPublicController {
    /**
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from   количество элементов, которые нужно пропустить для формирования
     *               текущего набора
     * @param size   количество элементов в наборе
     * @return Получение подборок событий
     *         В случае, если по заданным фильтрам не найдено ни одной подборки,
     *         возвращает пустой список
     */
    @GetMapping
    List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size);

    /**
     * @param compId id подборки
     * @return Получение подборки событий по его id
     *         В случае, если подборки с заданным id не найдено,
     *         возвращает статус код 404 ApiError.
     *         Запрос составлен некорректно - 400 ApiError
     */
    @GetMapping("{compId}")
    CompilationDto getCompilation(@Positive @PathVariable Integer compId);
}
