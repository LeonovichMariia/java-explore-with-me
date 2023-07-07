package ru.practicum.ewmmain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.Constants;
import ru.practicum.ewmmain.event.enums.StateActionUser;
import ru.practicum.ewmmain.event.model.Location;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "Аннотация должна содержать не менее 20 и не более 2000 символов")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Описание должно содержать не менее 20 и не более 7000 символов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.pattern)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateActionUser stateAction;
    @Size(min = 3, max = 120, message = "Заголовок должен содержать не менее 3 и не более 120 символов")
    private String title;
}
