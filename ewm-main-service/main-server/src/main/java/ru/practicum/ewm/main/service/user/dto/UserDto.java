package ru.practicum.ewm.main.service.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Must not be blank.")
    private String name;

    @NotBlank(message = "Must not be blank.")
    @Email(message = "The entered email is incorrect.")
    private String email;
}
