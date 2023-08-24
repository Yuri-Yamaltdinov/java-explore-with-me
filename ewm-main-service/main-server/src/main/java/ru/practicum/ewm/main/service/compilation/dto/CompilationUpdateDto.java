package ru.practicum.ewm.main.service.compilation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationUpdateDto {

    Set<Long> events;

    Boolean pinned;

    @Size(min = 1,
            max = 50,
            message = "Title length must be between 1 and 50 characters.")
    String title;
}