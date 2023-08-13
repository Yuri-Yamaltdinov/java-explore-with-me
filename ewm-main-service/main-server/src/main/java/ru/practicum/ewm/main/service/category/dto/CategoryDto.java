package ru.practicum.ewm.main.service.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Must not be blank.")
    private String name;
}
