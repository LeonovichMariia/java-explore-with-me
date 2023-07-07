package ru.practicum.ewmmain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.Constants;
import ru.practicum.ewmmain.comment.CommentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.pattern)
    private LocalDateTime createdOn;
    private Long eventId;
    private String eventTitle;
    private CommentStatus status;
}
