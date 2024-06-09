package ru.practicum.ewm.controller.categories.impl;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.controller.categories.CategoriesAdminController;
import ru.practicum.ewm.model.categories.dto.CategoryDto;
import ru.practicum.ewm.model.categories.dto.NewCategoryDto;
import ru.practicum.ewm.service.categories.CategoriesService;

@RestController
@RequiredArgsConstructor
public class CategoriesAdminControllerImpl implements CategoriesAdminController {

    private final CategoriesService service;

    @Override
    public CategoryDto setCategory(NewCategoryDto newCategoryDto) {
        return service.setCategory(newCategoryDto);
    }

    @Override
    public void deleteCategory(Integer catId) {
        service.deleteCategory(catId);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer catId) {
        return service.updateCategory(categoryDto, catId);
    }
}
