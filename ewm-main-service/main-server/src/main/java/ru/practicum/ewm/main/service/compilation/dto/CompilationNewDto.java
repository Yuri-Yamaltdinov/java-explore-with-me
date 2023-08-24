package ru.practicum.ewm.main.service.compilation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationNewDto {

    private Set<Long> events;

    private Boolean pinned;

    @NotBlank
    @Size(min = 1,
            max = 50,
            message = "Title length must be between 1 and 50 characters.")
    private String title;
}
