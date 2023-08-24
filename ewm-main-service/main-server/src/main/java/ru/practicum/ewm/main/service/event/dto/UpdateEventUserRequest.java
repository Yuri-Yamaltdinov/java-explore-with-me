package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.main.service.event.model.StateAction;
import ru.practicum.ewm.main.service.location.dto.LocationDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.main.service.util.Constants.DATE_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {

    @Nullable
    @Length(min = 20,
            max = 2000,
            message = "Annotation field size must be between 20 and 2000 characters.")
    private String annotation;

    private Long category;

    @Nullable
    @Length(min = 20,
            max = 7000,
            message = "Description field size must be between 20 and 7000 characters.")
    private String description;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Nullable
    @Length(min = 3,
            max = 120,
            message = "Title field size must be between 3 and 120 characters.")
    private String title;

}
