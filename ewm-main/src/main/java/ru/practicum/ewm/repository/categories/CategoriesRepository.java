package ru.practicum.ewm.repository.categories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.ewm.model.categories.Category;

public interface CategoriesRepository extends JpaRepository<Category, Integer> {
}
