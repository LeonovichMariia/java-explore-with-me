package ru.practicum.ewmmain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.Constants;
import ru.practicum.ewmmain.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Аннотация не может быть пустой")
    @Size(min = 20, max = 2000, message = "Аннотация должна содержать не менее 20 и не более 2000 символов")
    private String annotation;
    private Long category;
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 20, max = 7000, message = "Описание должно содержать не менее 20 и не более 7000 символов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private Boolean paid = false;
    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean requestModeration  = true;
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 120, message = "Заголовок должен содержать не менее 3 и не более 120 символов")
    private String title;
}
