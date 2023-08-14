package ru.practicum.ewm.main.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Must not be blank.")
    private String name;

    @NotBlank(message = "Must not be blank.")
    @Email(message = "The entered email is incorrect.")
    private String email;
}
