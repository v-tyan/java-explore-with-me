package ru.practicum.ewm.service.compilations.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.model.compilations.Compilation;
import ru.practicum.ewm.model.compilations.CompilationMapper;
import ru.practicum.ewm.model.compilations.dto.CompilationDto;
import ru.practicum.ewm.model.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.model.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm.model.errors.NotFoundException;
import ru.practicum.ewm.model.events.Event;
import ru.practicum.ewm.repository.compilations.CompilationsRepository;
import ru.practicum.ewm.repository.events.EventsRepository;
import ru.practicum.ewm.service.compilations.CompilationService;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationsRepository compilationsRepository;
    private final EventsRepository eventsRepository;

    @Override
    public CompilationDto setCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = eventsRepository.findByIds(newCompilationDto.getEvents());
        return CompilationMapper.toCompilationDto(
                compilationsRepository.save(CompilationMapper.toCompilation(newCompilationDto, events)));
    }

    @Override
    public void deleteCompilation(Integer compId) {
        compilationsRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        compilationsRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Integer compId) {
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventsRepository.findByIds(updateCompilationRequest.getEvents()));
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && !updateCompilationRequest.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        compilationsRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations = compilationsRepository.findAllByPinned(pinned, pageable);
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompilationDto> getCompilations(Pageable pageable) {
        List<Compilation> compilations = compilationsRepository.findAllBy(pageable);
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Integer compId) {
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
