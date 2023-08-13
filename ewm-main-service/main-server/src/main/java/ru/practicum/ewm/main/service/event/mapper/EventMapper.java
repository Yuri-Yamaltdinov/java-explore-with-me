package ru.practicum.ewm.main.service.event.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.category.service.CategoryService;
import ru.practicum.ewm.main.service.event.dto.EventFullDto;
import ru.practicum.ewm.main.service.event.dto.EventShortDto;
import ru.practicum.ewm.main.service.event.dto.NewEventDto;
import ru.practicum.ewm.main.service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.location.model.Location;
import ru.practicum.ewm.main.service.user.dto.UserShortDto;
import ru.practicum.ewm.main.service.user.model.User;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
    @Autowired
    protected CategoryService categoryService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category",
            expression = "java(categoryFromDto(categoryService.getCategory(newEventDto.getCategory()), newEventDto.getCategory()))")
    @Mapping(target = "initiator", source = "initiator")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    public abstract Event eventFromNewEventDto(NewEventDto newEventDto,
                                               User initiator,
                                               Location location);

    public abstract Category categoryFromDto(CategoryDto categoryDto, Long id);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category",
            expression = "java(categoryFromDto(categoryService.getCategory(eventDto.getCategory()), eventDto.getCategory()))")
    @Mapping(target = "location", source = "location")
    public abstract void updateEventFromDto(@MappingTarget Event event,
                                            UpdateEventUserRequest eventDto,
                                            Location location);

    public abstract EventFullDto eventFullDtoFromEvent(Event event, Long views);

    public abstract EventShortDto eventShortDtoFromEvent(Event event, Long views);

    public abstract CategoryDto categoryDtoFromCategory(Category category);

    public abstract UserShortDto userShortDtoFromUser(User user);
}
