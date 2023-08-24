package ru.practicum.ewm.main.service.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.location.model.Location;
import ru.practicum.ewm.main.service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.ewm.main.service.event.model.State.PENDING;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 2000, nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @Column(name = "confirmed_requests")
    @Builder.Default
    private Long confirmedRequests = 0L;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(length = 7000, nullable = false)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    @ToString.Exclude
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @ToString.Exclude
    private Location location;

    private Boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private State state = PENDING;

    @Column(length = 120, nullable = false)
    private String title;
}
