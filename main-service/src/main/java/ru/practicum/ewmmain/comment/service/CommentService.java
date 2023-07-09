package ru.practicum.ewmmain.comment.service;

import ru.practicum.ewmmain.comment.dto.CommentDto;
import ru.practicum.ewmmain.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto addComment(NewCommentDto newCommentDto, Long userId, Long eventId);

    CommentDto renewalComment(NewCommentDto newCommentDto, Long userId, Long commentId);

    CommentDto getCommentById(Long userId,  Long commentId);

    List<CommentDto> getAllUserComments(Long userId, Integer from, Integer size);

    void deleteCommentById(Long userId, Long commentId);

    List<CommentDto> getAllCommentsForEvent(Long eventId, Integer from, Integer size);

    CommentDto renewalCommentAdmin(Long commentId, NewCommentDto newCommentDto);

    void deleteCommentAdmin(Long commentId);
}
