package ru.practicum.ewm.main.service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.service.user.model.User;
import ru.practicum.ewm.main.service.util.Pagination;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(Long[] ids, Pagination page);
}
