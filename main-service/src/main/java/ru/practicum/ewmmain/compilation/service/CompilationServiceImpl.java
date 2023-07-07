package ru.practicum.ewmmain.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.compilation.dto.CompilationDto;
import ru.practicum.ewmmain.compilation.dto.NewCompilationDto;
import ru.practicum.ewmmain.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmmain.compilation.mapper.CompilationMapper;
import ru.practicum.ewmmain.compilation.model.Compilation;
import ru.practicum.ewmmain.compilation.repository.CompilationRepository;
import ru.practicum.ewmmain.event.repository.EventRepository;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.utils.PageSetup;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation newCompilation = CompilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null)
            newCompilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));
        CompilationDto savedCompilation = CompilationMapper.toCompilationDto(compilationRepository.save(newCompilation));
        log.info("Подборка событий {} сохранена", savedCompilation);
        return savedCompilation;
    }

    @Override
    public CompilationDto renewalCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        Compilation compilation = validateCompilation(compId);
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(updateCompilationRequest.getEvents()));
        }
        CompilationDto updatedCompilation = CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
        log.info("Подборка событий с id {} обновлена", compId);
        return updatedCompilation;
    }

    @Override
    public void deleteCompilationById(Long compId) {
        validateCompilation(compId);
        compilationRepository.deleteById(compId);
        log.info("Подборка событий с id {} удалена", compId);

    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, Integer size, Integer from) {
        log.info("Получение списка всех подборок событий");
        List<Compilation> compilations;
        PageRequest pageable = new PageSetup(from, size, Sort.unsorted());
        if (pinned) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).getContent();
        }
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compId) {
        log.info("Получение информации о подборке событий с id {}", compId);
        Compilation compilation = validateCompilation(compId);
        return CompilationMapper.toCompilationDto(compilation);
    }

    private Compilation validateCompilation(Long compId) {
        return compilationRepository.findCompilationById(compId).orElseThrow(() -> new NotFoundException(
                "Подборка событий с id " + compId + " не найдена"));
    }
}
