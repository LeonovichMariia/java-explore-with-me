package ru.practicum.ewmmain.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.compilation.model.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Optional<Compilation> findCompilationById(Long compId);

    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}
