package ru.practicum.ewmmain.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmain.event.dto.ParticipationRequestDto;
import ru.practicum.ewmmain.event.model.Request;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }
}
