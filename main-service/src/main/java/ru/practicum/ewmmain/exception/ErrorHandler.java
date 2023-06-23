package ru.practicum.ewmmain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException e) {
        log.info("Ошибка 400!");
        return Map.of("error", "Ошибка валидации",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        log.info("Ошибка 404!");
        return Map.of("error", "Искомый объект не найден",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyExistException(final AlreadyExistException e) {
        log.info("Ошибка 409!");
        return Map.of("error", "Такой объект уже существует",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowableException(final Throwable e) {
        log.error("Ошибка 500! {}", e.toString());
        return Map.of("error", "Internal Server Error");
    }
}
