package ru.practicum.ewmmain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.comment.CommentStatus;
import ru.practicum.ewmmain.comment.dto.CommentDto;
import ru.practicum.ewmmain.comment.dto.CommentDtoUpdate;
import ru.practicum.ewmmain.comment.dto.NewCommentDto;
import ru.practicum.ewmmain.comment.mapper.CommentMapper;
import ru.practicum.ewmmain.comment.model.Comment;
import ru.practicum.ewmmain.comment.repository.CommentRepository;
import ru.practicum.ewmmain.event.model.Event;
import ru.practicum.ewmmain.event.repository.EventRepository;
import ru.practicum.ewmmain.exception.ConflictException;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.user.model.User;
import ru.practicum.ewmmain.user.repository.UserRepository;
import ru.practicum.ewmmain.utils.PageSetup;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto addComment(NewCommentDto newCommentDto, Long userId, Long eventId) {
        User user = validateUser(userId);
        Event event = validateEvent(eventId);
        Comment comment = CommentMapper.toComment(newCommentDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setStatus(CommentStatus.PENDING);
        CommentDto savedComment = CommentMapper.toCommentDto(commentRepository.save(comment));
        log.info("Комментарий {} сохранен", savedComment);
        return savedComment;
    }

    @Override
    public CommentDto renewalComment(CommentDtoUpdate commentDtoUpdate, Long userId, Long commentId) {
        Comment comment = validateComment(commentId);
        validateUser(userId);
        if (!comment.getAuthor().getId().equals(userId)) {
            log.error("Только автор или администратор может обновить комментарий");
            throw new ConflictException("Только автор или администратор может обновить комментарий");
        }
        if (commentDtoUpdate.getText() != null) {
            comment.setText(commentDtoUpdate.getText());
        }
        comment.setStatus(CommentStatus.PENDING);
        CommentDto updatedComment = CommentMapper.toCommentDto(commentRepository.save(comment));
        log.info("Комментарий с id {} обновлен", commentId);
        return updatedComment;
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long userId, Long commentId) {
        log.info("Получение собственного комментария с id {}", commentId);
        validateUser(userId);
        Comment comment = validateComment(commentId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllUserComments(Long userId, Integer from, Integer size) {
        log.info("Получение списка всех комментарией пользователя с id {}", userId);
        validateUser(userId);
        PageRequest pageable = new PageSetup(from, size);
        List<Comment> comments = commentRepository.findAllByAuthorId(userId, pageable).getContent();
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .sorted(Comparator.comparing(CommentDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommentById(Long userId, Long commentId) {
        Comment comment = validateComment(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            log.error("Только автор или администратор может улалить комментарий");
            throw new ConflictException("Только автор или администратор может обновить комментарий");
        }
        commentRepository.deleteById(commentId);
        log.info("Комментарий с id {} удален", commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsForEvent(Long eventId, Integer from, Integer size) {
        log.info("Получение списка комментариев о событии с id {}", eventId);
        validateEvent(eventId);
        PageRequest pageable = new PageSetup(from, size);
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageable).getContent();
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto renewalCommentAdmin(Long commentId, CommentDtoUpdate commentDtoUpdate) {
        Comment comment = validateComment(commentId);
        if (commentDtoUpdate.getText() != null) {
            comment.setText(commentDtoUpdate.getText());
        }
        comment.setStatus(CommentStatus.PUBLISHED);
        CommentDto updatedComment = CommentMapper.toCommentDto(commentRepository.save(comment));
        log.info("Комментарий с id {} обновлен администратором", commentId);
        return updatedComment;
    }

    @Override
    public void deleteCommentAdmin(Long commentId) {
        validateComment(commentId);
        commentRepository.deleteById(commentId);
        log.info("Комментарий с id " + commentId + "удален");
    }

    private User validateUser(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(
                "Пользователь с id " + userId + " не найден"));
    }

    private Event validateEvent(Long eventId) {
        return eventRepository.findEventById(eventId).orElseThrow(() -> new NotFoundException(
                "Событие с id " + eventId + " не найдено"));
    }

    private Comment validateComment(Long commentId) {
        return commentRepository.findCommentById(commentId).orElseThrow(() -> new NotFoundException(
                "Комментарий с id " + commentId + " не найден"));
    }
}
