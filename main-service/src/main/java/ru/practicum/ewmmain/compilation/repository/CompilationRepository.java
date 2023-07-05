package ru.practicum.ewmmain.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.compilation.model.Compilation;
import ru.practicum.ewmmain.exception.NotFoundException;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    default Compilation validateCompilation(Long compId) {
        return findById(compId).orElseThrow(() -> new NotFoundException(
                "Подборка событий с id " + compId + " не найдена"));
    }

    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}
