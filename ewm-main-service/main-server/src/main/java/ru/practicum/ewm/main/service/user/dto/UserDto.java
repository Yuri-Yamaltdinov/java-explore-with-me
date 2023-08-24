package ru.practicum.ewm.main.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Must not be blank.")
    private String name;

    @NotBlank(message = "Must not be blank.")
    @Email(message = "The entered email is incorrect.")
    @Size(min = 6,
            max = 254,
            message = "Email field size must be between 6 and 254 characters.")
    private String email;
}
