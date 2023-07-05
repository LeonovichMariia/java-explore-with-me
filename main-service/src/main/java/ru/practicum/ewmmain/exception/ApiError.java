package ru.practicum.ewmmain.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.Constants;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApiError {
    private String errors;
    private String message;
    private String reason;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.PATTERN)
    public LocalDateTime timestamp;
}
