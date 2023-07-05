package ru.practicum.ewmmain.exception;

public class EventIsNotPublishedException extends RuntimeException {
    public EventIsNotPublishedException(String message) {
        super(message);
    }
}
