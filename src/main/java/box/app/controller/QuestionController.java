package box.app.controller;

import box.app.dto.question.QuestionDto;
import box.app.dto.question.QuestionRequestDto;
import box.app.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Question management", description = "Endpoints for questions managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/questions")
public class QuestionController {
    private final QuestionService questionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new question", description = "Add a new question")
    public QuestionDto addCoach(@RequestBody @Valid QuestionRequestDto requestDto) {
        return questionService.add(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all questions", description = "Get a list of all questions")
    public List<QuestionDto> getAll(Pageable pageable) {
        return questionService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the question by ID", description = "Get the question by ID")
    public QuestionDto getQuestionById(@PathVariable Long id) {
        return questionService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update the question by ID",
            description = "Update the question by ID")
    public QuestionDto updateQuestionById(@PathVariable Long id,
                                    @RequestBody QuestionRequestDto requestDto) {
        return questionService.updateById(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the question by ID", description = "Delete the question by ID")
    public void deleteById(@PathVariable Long id) {
        questionService.deleteById(id);
    }
}
