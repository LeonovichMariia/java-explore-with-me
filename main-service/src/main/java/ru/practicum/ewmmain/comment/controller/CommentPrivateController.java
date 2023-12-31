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

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({Marker.OnCreate.class})
    public CommentDto addComment(@RequestBody @Valid NewCommentDto newCommentDto,
                                 @PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Запрос на добавление комментария о событии с id {} от пользователя с id {}", eventId, userId);
        return commentService.addComment(newCommentDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    @Validated({Marker.OnUpdate.class})
    public CommentDto renewalComment(@RequestBody @Valid NewCommentDto newCommentDto,
                                     @PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Запрос на обновление комментария о событии с id {} от пользователя с id {}", commentId, userId);
        return commentService.renewalComment(newCommentDto, userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Запрос на получение комментария с id {} от пользователя с id {}", commentId, userId);
        return commentService.getCommentById(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getAllUserComments(@PathVariable Long userId,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from) {
        log.info("Запрос на списка комментариев от пользователя с id {}", userId);
        return commentService.getAllUserComments(userId, from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Запрос на удаление комментария с id {} от пользователя с id {}", commentId, userId);
        commentService.deleteCommentById(userId, commentId);
    }
}
