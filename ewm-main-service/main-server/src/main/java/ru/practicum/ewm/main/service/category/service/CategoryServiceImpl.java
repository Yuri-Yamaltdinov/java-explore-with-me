package ru.practicum.ewm.main.service.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.mapper.CategoryMapper;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.category.repository.CategoryRepository;
import ru.practicum.ewm.main.service.exception.EntityNotFoundException;
import ru.practicum.ewm.main.service.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public void delete(Long catId) {
        getOrThrow(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        try {
            Category category = categoryMapper.categoryFromDto(categoryDto);
            Category categorySaved = categoryRepository.save(category);
            return categoryMapper.categoryToDto(categorySaved);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Category with specified name is already exists.");
        }
    }

    @Override
    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        Category category = getOrThrow(catId);
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryMapper.categoryToDto(categoryRepository.saveAndFlush(category));
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);

        return categoryRepository.findAll(page)
                .stream()
                .map(categoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return categoryMapper.categoryToDto(getOrThrow(catId));
    }

    private Category getOrThrow(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(Category.class, String.format("ID: %s", catId)));
    }
}
