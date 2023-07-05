package ru.practicum.ewmmain.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned = false;
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 1, max = 50, message = "Заголовок должен содержать не менее 1 и не более 50 символов")
    private String title;
}
