package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.service.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.main.service.util.Constants.DATE_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(min = 20,
            max = 2000,
            message = "Annotation field size must be between 20 and 2000 characters.")
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20,
            max = 7000,
            message = "Description field size must be between 20 and 7000 characters.")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotNull
    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    private Long participantLimit = 0L;

    @Builder.Default
    private Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3,
            max = 120,
            message = "Title field size must be between 3 and 120 characters.")
    private String title;
}
