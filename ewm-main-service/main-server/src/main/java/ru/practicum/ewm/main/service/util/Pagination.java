package ru.practicum.ewm.main.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class Pagination extends PageRequest {
    private Integer from;
    private Integer size;

    public Pagination(int from, int size) {
        super(from > 0 ? from / size : 0, size, Sort.unsorted());
    }
}
