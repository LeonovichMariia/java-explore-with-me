package ru.practicum.ewmmain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
//    @Email(message = "Некорректный email")
//    @NotBlank(message = "email не может быть пустым")
    private String email;
    private Long id;
//    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;
}
