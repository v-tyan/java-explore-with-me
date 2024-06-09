package ru.practicum.ewm.controller.compilations;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.ewm.model.compilations.dto.CompilationDto;
import ru.practicum.ewm.model.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.model.compilations.dto.UpdateCompilationRequest;

/**
 * API для работы с подборками событий
 */
@Validated
@RestController
@RequestMapping("/admin/compilations")
public interface CompilationsAdminController {

    /**
     * @param newCompilationDto данные новой подборки
     * @return 201 - Подборка добавлена CompilationDto
     *         400 - Запрос составлен некорректно ApiError
     *         409 - Нарушение целостности данных ApiError
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto setCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto);

    /**
     * @param compId id подборки
     *               204 - Подборка удалена
     *               404 - Подборка не найдена или недоступна ApiError
     */
    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@Positive @PathVariable Integer compId);

    /**
     * @param updateCompilationRequest данные для обновления подборки
     * @param compId                   id подборки
     * @return 200 Подборка обновлена CompilationDto
     *         404 Подборка не найдена или недоступна ApiError
     */
    @PatchMapping("{compId}")
    CompilationDto updateCompilation(
            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest,
            @PathVariable Integer compId);
}
