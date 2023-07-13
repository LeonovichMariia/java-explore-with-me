package ru.practicum.ewmmain.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.comment.dto.CommentDto;
import ru.practicum.ewmmain.comment.dto.NewCommentDto;
import ru.practicum.ewmmain.comment.service.CommentService;
import ru.practicum.ewmmain.utils.Marker;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentAdminController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    @Validated({Marker.OnUpdate.class})
    public CommentDto renewalCommentAdmin(@RequestBody NewCommentDto newCommentDto,
                                          @PathVariable Long commentId) {
        log.info("Запрос на обновление комментария с id {} администратором", commentId);
        return commentService.renewalCommentAdmin(commentId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable Long commentId) {
        log.info("Запрос на удаление комментария с id {} администратором", commentId);
        commentService.deleteCommentAdmin(commentId);
    }
}
