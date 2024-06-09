package ru.practicum.ewm.controller.categories.impl;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.controller.categories.CategoriesController;
import ru.practicum.ewm.model.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.CategoriesService;

@RestController
@RequiredArgsConstructor
public class CategoriesControllerImpl implements CategoriesController {

    private final CategoriesService service;

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return service.getCategories(pageable);
    }

    public CategoryDto getCategory(Integer catId) {
        return service.getCategory(catId);
    }
}
