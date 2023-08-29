package ru.practicum.ewm.main.service.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Must not be blank.")
    @Size(min = 1,
            max = 50,
            message = "Name field size must be between 1 and 50 characters.")
    private String name;
}
