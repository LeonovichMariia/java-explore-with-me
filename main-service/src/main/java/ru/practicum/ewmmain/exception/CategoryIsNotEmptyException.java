package ru.practicum.ewmmain.exception;

public class CategoryIsNotEmptyException extends RuntimeException {
    public CategoryIsNotEmptyException(String message) {
        super(message);
    }
}