package ru.practicum.ewmmain.exception;

public class WrongUserException extends RuntimeException {

    public WrongUserException(String message) {
        super(message);
    }
}
