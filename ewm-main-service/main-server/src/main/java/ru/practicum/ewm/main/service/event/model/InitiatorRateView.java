package ru.practicum.ewm.main.service.event.model;

import java.util.Optional;

public interface InitiatorRateView {

    Optional<Long> getInitiatorId();

    Optional<String> getName();

    Optional<Long> getRatesSum();

}
