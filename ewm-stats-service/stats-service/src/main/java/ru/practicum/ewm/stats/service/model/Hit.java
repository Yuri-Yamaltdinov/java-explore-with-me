package ru.practicum.ewm.stats.service.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String app;

    @Column(length = 512)
    @NotBlank
    private String uri;

    @Column(length = 20)
    @NotBlank
    private String ip;

    @Column(name = "hit_time")
    private LocalDateTime timestamp;
}
