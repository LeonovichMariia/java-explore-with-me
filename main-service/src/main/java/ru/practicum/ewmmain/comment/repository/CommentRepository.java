package ru.practicum.ewmmain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.comment.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findCommentById(Long commentId);

    Boolean existsByIdAndAuthorId(Long commentId, Long userId);

    Page<Comment> findAllByAuthorId(Long authorId, PageRequest pageable);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long userId);

    Page<Comment> findAllByEventId(Long eventId, PageRequest pageable);
}
