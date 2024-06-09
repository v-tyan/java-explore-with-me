package ru.practicum.ewm.model.compilations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.model.compilations.dto.CompilationDto;
import ru.practicum.ewm.model.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.model.events.Event;
import ru.practicum.ewm.model.events.EventMapper;
import ru.practicum.ewm.model.events.dto.EventShortDto;

@UtilityClass
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {

        if (newCompilationDto.getEvents() == null) {
            newCompilationDto.setEvents(new ArrayList<>());
        }
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .events(events).build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> shortEvents = compilation.getEvents().stream()
                .map(EventMapper.EVENT_MAPPER::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(shortEvents)
                .title(compilation.getTitle())
                .pinned(compilation.getPinned()).build();
    }
}
