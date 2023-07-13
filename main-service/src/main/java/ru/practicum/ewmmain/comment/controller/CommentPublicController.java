package ru.practicum.ewmmain.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.comment.dto.CommentDto;
import ru.practicum.ewmmain.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getAllCommentsForEvent(@PathVariable Long eventId,
                                                   @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                                   @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from) {
        log.info("Запрос на получение всех комментариев о событии с id {}", eventId);
        return commentService.getAllCommentsForEvent(eventId, from, size);
    }
}
