package ru.practicum.ewm.main.service.compilation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationUpdateDto {

    List<Long> events;

    Boolean pinned;

    @Length(min = 1,
            max = 20,
            message = "Title length must be between 1 and 20 characters.")
    String title;
}