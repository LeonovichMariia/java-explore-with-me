package ru.practicum.ewmmain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException e) {
        log.info("Ошибка 400!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Неверно составлен запрос",
                HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("Ошибка 404!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Искомый объект не найден",
                HttpStatus.NOT_FOUND.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExistException(final AlreadyExistException e) {
        log.info("Ошибка 409!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Такой объект уже существует",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowableException(final Throwable e) {
        log.error("Ошибка 500! {}", e.toString());
        return new ApiError(
                getAsString((Exception) e),
                e.getMessage(),
                "Необрабатываемое исключение",
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryIsNotEmptyException(final CategoryIsNotEmptyException e) {
        log.info("Ошибка 409!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "У категории есть связанные события",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleWrongUserException(final WrongUserException e) {
        log.info("Ошибка 409!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Запрос на участие в собственном событии",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventIsNotPublishedException(final EventIsNotPublishedException e) {
        log.info("Ошибка 409!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Событие не опубликовано",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipantLimitException(final ParticipantLimitException e) {
        log.info("Ошибка 409!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Достигнут лимит возможных участников",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.info("Ошибка 409!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Несовпадение значений",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.info("Ошибка 409!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Constraint violation exception",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.info("Ошибка 409!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Integrity constraint has been violated",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException e) {
        log.info("Ошибка 400!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Ошибка валидации данных",
                HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MissingServletRequestParameterException e) {
        log.info("Ошибка 400!");
        return new ApiError(
                getAsString(e),
                e.getMessage(),
                "Missing servlet request parameter exception",
                HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now());
    }

    private String getAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
