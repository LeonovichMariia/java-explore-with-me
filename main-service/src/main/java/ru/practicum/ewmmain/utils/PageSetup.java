package ru.practicum.ewmmain.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageSetup extends PageRequest {
    public PageSetup(Integer from, Integer size, Sort sort) {
        super(from / size, size, sort);
    }

    public PageSetup(Integer from, Integer size) {
        super(from / size, size, Sort.unsorted());
    }
}
