package ru.practicum.ewmmain.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.compilation.dto.CompilationDto;
import ru.practicum.ewmmain.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from) {
        log.info("Запрос на получение списка всех подборок событий: from={}, size={}", size, from);
        return compilationService.getCompilations(pinned, size, from);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Запрос на получение подборки событий с id {}", compId);
        return compilationService.getCompilationById(compId);
    }
}
