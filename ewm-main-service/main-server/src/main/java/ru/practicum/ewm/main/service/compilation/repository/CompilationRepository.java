package ru.practicum.ewm.main.service.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.service.compilation.model.Compilation;
import ru.practicum.ewm.main.service.util.Pagination;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllByPinned(Boolean pinned, Pagination page);
}
