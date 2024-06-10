package ru.practicum.ewm.controller.categories;

import java.util.List;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.CategoriesService;

/**
 * Публичный API для работы с категориями
 */
@Validated
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoriesService service;

    /**
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает
     * пустой список
     * 
     * @param from количество категорий, которые нужно пропустить для формирования
     *             текущего набора
     * @param size количество категорий в наборе
     * @return List<CategoryDto>
     *         Категории найдены - 200
     *         Запрос составлен некорректно - 400
     */
    @GetMapping
    List<CategoryDto> getCategories(
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return service.getCategories(pageable);
    }

    /**
     * В случае, если категории с заданным id не найдено, возвращает статус код 404
     * 
     * @param catId id категории
     * @return CategoryDto
     *         200 - Категория найдена
     *         400 - Запрос составлен некорректно
     *         404 - Категория не найдена или недоступна
     */
    @GetMapping("{catId}")
    CategoryDto getCategory(@Positive @PathVariable Integer catId) {
        return service.getCategory(catId);
    }
}
