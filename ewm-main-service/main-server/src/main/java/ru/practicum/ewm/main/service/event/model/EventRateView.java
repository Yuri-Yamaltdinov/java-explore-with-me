package ru.practicum.ewm.main.service.event.model;

import java.util.Optional;

public interface EventRateView {

    Optional<Long> getEventId();

    Optional<String> getTitle();

    Long getRatesSum();

}
