package ru.practicum.ewmmain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmmain.utils.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewCommentDto {
    @Null(groups =  {Marker.OnCreate.class, Marker.OnUpdate.class})
    private Long id;
    @NotBlank(message = "Текст комментария не может быть пустым",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Size(min = 1, max = 7000, message = "Текст комментария должен содержать не менее 1 и не более 7000 символов",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String text;
}
