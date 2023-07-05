package ru.practicum.dto;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {
    public final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);
}
