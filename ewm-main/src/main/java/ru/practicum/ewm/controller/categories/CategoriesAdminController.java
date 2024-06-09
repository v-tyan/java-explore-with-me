package ru.practicum.ewm.controller.categories;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.ewm.model.categories.dto.CategoryDto;
import ru.practicum.ewm.model.categories.dto.NewCategoryDto;

/**
 * API для работы с категориями
 */
@Validated
@RestController
@RequestMapping("/admin/categories")
public interface CategoriesAdminController {

    /**
     * @param newCategoryDto данные добавляемой категории
     * @return имя категории должно быть уникальным
     *         201 - Категория добавлена CategoryDto
     *         400 - Запрос составлен некорректно ApiError
     *         409 - Нарушение целостности данных ApiError
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto setCategory(@Valid @RequestBody NewCategoryDto newCategoryDto);

    /**
     * @param catId id категории
     */
    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@Positive @PathVariable Integer catId);

    /**
     * @param categoryDto Данные категории для изменения
     * @param catId       id категории
     * @return имя категории должно быть уникальным
     *         200 - Данные категории изменены
     *         404 - Категория не найдена или недоступна ApiError
     *         409 - Нарушение целостности данных ApiError
     */
    @PatchMapping("{catId}")
    CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto,
            @Positive @PathVariable Integer catId);
}
