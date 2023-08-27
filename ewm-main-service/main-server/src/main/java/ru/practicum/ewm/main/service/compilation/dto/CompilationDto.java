package ru.practicum.ewm.main.service.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;

import java.util.Set;

@Data
@Builder
public class CompilationDto {

    private Set<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
