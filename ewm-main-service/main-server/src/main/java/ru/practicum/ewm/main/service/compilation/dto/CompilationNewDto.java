package ru.practicum.ewm.main.service.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationNewDto {

    @NotNull
    List<Long> events;

    @NotNull
    @Builder.Default
    Boolean pinned = false;

    @NotNull
    @Length(min = 1,
            max = 50,
            message = "Title length must be between 1 and 50 characters.")
    String title;
}
