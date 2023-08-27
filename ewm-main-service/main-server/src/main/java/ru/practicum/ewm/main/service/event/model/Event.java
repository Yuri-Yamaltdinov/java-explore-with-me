package ru.practicum.ewm.main.service.event.model;

import lombok.*;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.location.model.Location;
import ru.practicum.ewm.main.service.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static ru.practicum.ewm.main.service.event.model.State.PENDING;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(length = 2000)
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

    @NotBlank
    @Column(length = 7000)
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

    @NotBlank
    @Column(length = 120)
    private String title;
}
