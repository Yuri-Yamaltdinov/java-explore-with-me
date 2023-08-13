package ru.practicum.ewm.main.service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.user.model.User;
import ru.practicum.ewm.main.service.util.Pagination;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    List<Event> findAllByInitiator(User user, Pagination page);
}
