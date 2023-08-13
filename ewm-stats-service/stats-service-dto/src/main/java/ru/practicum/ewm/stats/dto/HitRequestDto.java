package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.dto.util.Constants.DATE_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitRequestDto {
    @NotBlank(message = "Field app must be filled.")
    private String app;
    @NotBlank(message = "Field uri must be filled.")
    private String uri;
    @NotBlank(message = "Field ip must be filled.")
    private String ip;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime timestamp;
}
