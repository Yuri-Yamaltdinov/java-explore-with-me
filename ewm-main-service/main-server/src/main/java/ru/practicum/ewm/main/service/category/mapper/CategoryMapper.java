package ru.practicum.ewm.main.service.category.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryFromDto(CategoryDto categoryDto);

    CategoryDto categoryToDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);
}
