package ru.practicum.ewmmain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewUserRequest {
    @Size(min = 6, max = 254, message = "email должна содержать не менее 6 и не более 254 символов")
    @Email(message = "Некорректный email")
    @NotBlank(message = "email не может быть пустым")
    private String email;
    @Size(min = 2, max = 250, message = "Имя должно содержать не менее 2 и не более 250 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;
}
