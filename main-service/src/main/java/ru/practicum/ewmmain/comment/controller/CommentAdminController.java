package ru.practicum.ewmmain.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.comment.dto.CommentDto;
import ru.practicum.ewmmain.comment.dto.CommentDtoUpdate;
import ru.practicum.ewmmain.comment.service.CommentService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class CommentAdminController {
    private final CommentService commentService;

    @PatchMapping("{userId}/comments/{commentId}")
    public CommentDto renewalCommentAdmin(@RequestBody @Valid CommentDtoUpdate updateComment,
                                          @PathVariable Long userId,
                                          @PathVariable Long commentId) {
        log.info("Запрос на обновление комментария с id {} от пользователя с id {} администратором", userId, commentId);
        return commentService.renewalCommentAdmin(userId, commentId, updateComment);
    }

    @DeleteMapping("{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable Long userId,
                                   @PathVariable Long commentId) {
        log.info("Запрос на удаление комментария с id {} от пользователя с id {} администратором", userId, commentId);
        commentService.deleteCommentAdmin(commentId, userId);
    }
}
