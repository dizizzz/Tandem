package box.app.controller;

import box.app.dto.coach.CoachDto;
import box.app.dto.coach.CoachRequestDto;
import box.app.service.CoachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Coach management", description = "Endpoints for coaches managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/coaches")
public class CoachController {
    private final CoachService coachService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new coach", description = "Add a new coach")
    public CoachDto addCoach(@RequestBody @Valid CoachRequestDto requestDto) {
        return coachService.add(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all coaches", description = "Get a list of all coaches")
    public List<CoachDto> getAll(Pageable pageable) {
        return coachService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the coach by ID", description = "Get coach's detailed information")
    public CoachDto getCoachById(@PathVariable Long id) {
        return coachService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update the coach by ID",
            description = "Update the coach by ID")
    public CoachDto updateCoachById(@PathVariable Long id,
                                @RequestBody CoachRequestDto requestDto) {
        return coachService.updateById(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the coach by ID", description = "Delete the coach by ID")
    public void deleteById(@PathVariable Long id) {
        coachService.deleteById(id);
    }

}
