package ru.practicum.ewm.stats.service.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String app;

    @Column(length = 512, nullable = false)
    String uri;

    @Column(length = 20, nullable = false)
    String ip;

    @Column(name = "hit_time")
    LocalDateTime timestamp;
}
