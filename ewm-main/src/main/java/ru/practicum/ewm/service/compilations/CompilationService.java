package ru.practicum.ewm.service.compilations;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm.model.compilations.dto.CompilationDto;
import ru.practicum.ewm.model.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.model.compilations.dto.UpdateCompilationRequest;

@Transactional(readOnly = true)
public interface CompilationService {
    @Transactional
    CompilationDto setCompilation(NewCompilationDto newCompilationDto);

    @Transactional
    void deleteCompilation(Integer compId);

    @Transactional
    CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Integer compId);

    List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable);

    List<CompilationDto> getCompilations(Pageable pageable);

    CompilationDto getCompilation(Integer compId);
}
