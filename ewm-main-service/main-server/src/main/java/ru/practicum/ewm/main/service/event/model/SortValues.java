package ru.practicum.ewm.main.service.event.model;

import java.util.Optional;

public enum SortValues {
    EVENT_DATE,
    VIEWS,
    RATES;

    public static Optional<SortValues> from(String sort) {
        for (SortValues state : values()) {
            if (state.name().equalsIgnoreCase(sort)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
