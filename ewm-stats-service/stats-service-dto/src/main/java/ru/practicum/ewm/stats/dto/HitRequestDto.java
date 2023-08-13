package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class HitRequestDto {
    @NotBlank(message = "Field app must be filled.")
    private String app;
    @NotBlank(message = "Field uri must be filled.")
    private String uri;
    @NotBlank(message = "Field ip must be filled.")
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
