package ru.practicum.ewm.main.service.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class CompilationUpdateDto {

    private Set<Long> events;

    private Boolean pinned;

    @Size(min = 1,
            max = 50,
            message = "Title length must be between 1 and 50 characters.")
    private String title;
}