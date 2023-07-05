package ru.practicum.ewmmain.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmain.compilation.dto.CompilationDto;
import ru.practicum.ewmmain.compilation.dto.NewCompilationDto;
import ru.practicum.ewmmain.compilation.model.Compilation;
import ru.practicum.ewmmain.event.mapper.EventMapper;

import java.util.Collections;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(compilation.getEvents() == null ? Collections.emptyList() :
                        compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
