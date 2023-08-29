package ru.practicum.ewm.main.service.category.service;

import ru.practicum.ewm.main.service.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    void delete(Long catId);

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(Long catId, CategoryDto categoryDto);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
