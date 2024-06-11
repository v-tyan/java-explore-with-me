package ru.practicum.ewm.service.categories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm.model.categories.dto.CategoryDto;
import ru.practicum.ewm.model.categories.dto.NewCategoryDto;

@Transactional(readOnly = true)
public interface CategoriesService {
    @Transactional
    CategoryDto setCategory(NewCategoryDto newCategoryDto);

    @Transactional
    void deleteCategory(Integer catId);

    @Transactional
    CategoryDto updateCategory(CategoryDto categoryDto, Integer catId);

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategory(Integer catId);
}
