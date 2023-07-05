package ru.practicum.ewmmain.compilation.service;

import ru.practicum.ewmmain.compilation.dto.CompilationDto;
import ru.practicum.ewmmain.compilation.dto.NewCompilationDto;
import ru.practicum.ewmmain.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto renewalCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);

    void deleteCompilationById(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer size, Integer from);

    CompilationDto getCompilationById(Long compId);
}
