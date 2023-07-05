package ru.practicum.ewmmain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.Constants;
import ru.practicum.ewmmain.event.enums.EventState;
import ru.practicum.ewmmain.event.model.Location;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventFullDto {
    @JsonUnwrapped
    private EventShortDto eventShortDto = new EventShortDto();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.pattern)
    private LocalDateTime createdOn;
    private String description;
    private Location location;
    private Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.pattern)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
}
