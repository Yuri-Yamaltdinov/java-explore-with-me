package ru.practicum.ewm.main.service.location.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {
    private Float lat;
    private Float lon;
}
