package ru.practicum.ewmmain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewCategoryDto {
    @Size(min = 1, max = 50)
    @NotBlank(message = "Название категории не может быть пустым")
    private String name;
}
