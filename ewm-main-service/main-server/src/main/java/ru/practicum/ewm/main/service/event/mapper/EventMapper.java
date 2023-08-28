package ru.practicum.ewm.main.service.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.category.service.CategoryService;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.dto.NewEventDto;
import ru.practicum.ewm.main.service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.location.dto.LocationDto;
import ru.practicum.ewm.main.service.location.model.Location;
import ru.practicum.ewm.main.service.location.repository.LocationRepository;
import ru.practicum.ewm.main.service.user.dto.UserShortDto;
import ru.practicum.ewm.main.service.user.model.User;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected LocationRepository locationRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "newEventDto", qualifiedByName = "categoryFromNewEventDto")
    @Mapping(target = "initiator", source = "initiator")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    public abstract Event eventFromNewEventDto(NewEventDto newEventDto,
                                               User initiator,
                                               Location location);

    @Named("categoryFromNewEventDto")
    public Category categoryFromNewEventDto(NewEventDto newEventDto) {
        return categoryFromDto(categoryService.getCategory(newEventDto.getCategory()));
    }

    public void updateEventFromURDto(Event event, UpdateEventUserRequest eventURDto) {

        if (eventURDto == null) {
            return;
        }

        if (eventURDto.getAnnotation() != null && !eventURDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventURDto.getAnnotation());
        }
        if (eventURDto.getDescription() != null && !eventURDto.getDescription().isBlank()) {
            event.setDescription(eventURDto.getDescription());
        }
        if (eventURDto.getEventDate() != null) {
            event.setEventDate(eventURDto.getEventDate());
        }
        if (eventURDto.getPaid() != null) {
            event.setPaid(eventURDto.getPaid());
        }
        if (eventURDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventURDto.getParticipantLimit());
        }
        if (eventURDto.getRequestModeration() != null) {
            event.setRequestModeration(eventURDto.getRequestModeration());
        }
        if (eventURDto.getTitle() != null && !eventURDto.getTitle().isBlank()) {
            event.setTitle(eventURDto.getTitle());
        }

        if (eventURDto.getLocation() != null) {
            event.setLocation(locationRepository.save(locationFromDto(eventURDto.getLocation())));
        }

        if (eventURDto.getCategory() != null) {
            event.setCategory(categoryFromDto(categoryService.getCategory(eventURDto.getCategory())));
        }
    }

    public EventFullDto eventFullDtoFromEvent(Event event, Long views) {
        if (event == null) {
            return null;
        }

        EventFullDto.EventFullDtoBuilder eventFullDto = EventFullDto.builder();

        eventFullDto.annotation(event.getAnnotation());
        eventFullDto.category(categoryDtoFromCategory(event.getCategory()));
        eventFullDto.confirmedRequests(event.getConfirmedRequests());
        eventFullDto.createdOn(event.getCreatedOn());
        eventFullDto.description(event.getDescription());
        eventFullDto.eventDate(event.getEventDate());
        eventFullDto.id(event.getId());
        eventFullDto.initiator(userShortDtoFromUser(event.getInitiator()));
        eventFullDto.location(locationToLocationDto(event.getLocation()));
        eventFullDto.paid(event.getPaid());
        eventFullDto.participantLimit(event.getParticipantLimit());
        eventFullDto.publishedOn(event.getPublishedOn());
        eventFullDto.requestModeration(event.getRequestModeration());
        eventFullDto.state(event.getState());
        eventFullDto.title(event.getTitle());
        eventFullDto.views(views);

        return eventFullDto.build();
    }

    public abstract Location locationFromDto(LocationDto locationDto);

    public EventShortDto eventShortDtoFromEvent(Event event, Long views) {
        if (event == null) {
            return null;
        }

        EventShortDto.EventShortDtoBuilder eventShortDto = EventShortDto.builder();

        eventShortDto.annotation(event.getAnnotation());
        eventShortDto.category(categoryDtoFromCategory(event.getCategory()));
        eventShortDto.confirmedRequests(event.getConfirmedRequests());
        eventShortDto.eventDate(event.getEventDate());
        eventShortDto.id(event.getId());
        eventShortDto.initiator(userShortDtoFromUser(event.getInitiator()));
        eventShortDto.paid(event.getPaid());
        eventShortDto.title(event.getTitle());

        eventShortDto.views(views);

        return eventShortDto.build();
    }

    public abstract CategoryDto categoryDtoFromCategory(Category category);

    public abstract Category categoryFromDto(CategoryDto categoryDto);

    public abstract UserShortDto userShortDtoFromUser(User user);

    protected abstract LocationDto locationToLocationDto(Location location);

}
