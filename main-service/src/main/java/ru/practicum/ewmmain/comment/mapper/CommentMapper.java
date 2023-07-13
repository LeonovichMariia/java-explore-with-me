package ru.practicum.ewmmain.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmain.comment.dto.CommentDto;
import ru.practicum.ewmmain.comment.dto.NewCommentDto;
import ru.practicum.ewmmain.comment.model.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .eventId(comment.getEvent().getId())
                .eventTitle(comment.getEvent().getTitle())
                .text(comment.getText())
                .createdOn(comment.getCreated())
                .status(comment.getStatus())
                .build();
    }
}
