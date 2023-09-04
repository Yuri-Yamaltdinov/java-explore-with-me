package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.main.service.util.Constants.DATE_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;

    private Long ratesSum;
}
