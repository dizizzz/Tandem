package box.app.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import box.app.dto.question.QuestionDto;
import box.app.dto.question.QuestionRequestDto;
import box.app.exception.EntityNotFoundException;
import box.app.mapper.QuestionMapper;
import box.app.model.question.Language;
import box.app.model.question.Question;
import box.app.repository.QuestionRepository;
import box.app.service.impl.QuestionServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private QuestionRepository repository;
    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private Question question;
    private QuestionDto questionDto;
    private QuestionRequestDto requestDto;

    @BeforeEach
    public void setup() {
        question = new Question();
        question.setId(1L);
        question.setQuestion("");
        question.setShortAnswer("");
        question.setFullAnswer("");
        question.setLanguage(Language.eng);

        questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        questionDto.setQuestion(question.getQuestion());
        questionDto.setShortAnswer(question.getShortAnswer());
        questionDto.setFullAnswer(question.getFullAnswer());
        questionDto.setLanguage(question.getLanguage());
    }

    @Test
    @DisplayName("Verify the correct question was added")
    public void addQuestion_WithValidQuestionRequestDto_ReturnQuestionDto() {
        requestDto = new QuestionRequestDto()
                .setQuestion(question.getQuestion())
                .setShortAnswer(question.getShortAnswer())
                .setFullAnswer(question.getFullAnswer())
                .setLanguage(question.getLanguage());

        when(repository.save(question)).thenReturn(question);
        when(questionMapper.toDto(question)).thenReturn(questionDto);
        when(questionMapper.toModel(requestDto)).thenReturn(question);

        QuestionDto actual = questionService.add(requestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(question.getId(), actual.getId());
    }

    @Test
    @DisplayName("Find all English questions")
    void findAllEngQuestions_WithValidLanguage_ReturnQuestionList() {
        Pageable pageable = PageRequest.of(0, 4);

        when(repository.findByLanguage(Language.eng, pageable)).thenReturn(List.of(question));
        List<QuestionDto> questionList = questionService.findAllEngQuestions(pageable);

        Assertions.assertEquals(1, questionList.size());
    }

    @Test
    @DisplayName("Find all Ukrainian questions")
    void findAllUkrQuestions_WithValidLanguage_ReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 4);

        when(repository.findByLanguage(Language.ukr, pageable)).thenReturn(List.of());
        List<QuestionDto> questionList = questionService.findAllUkrQuestions(pageable);

        Assertions.assertEquals(0, questionList.size());
        verify(repository, times(1)).findByLanguage(Language.ukr, pageable);
        verify(questionMapper, never()).toDto(any(Question.class));
    }

    @Test
    @DisplayName("Find all German questions")
    void findAllDeuQuestions_WithValidLanguage_ReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 4);

        when(repository.findByLanguage(Language.deu, pageable)).thenReturn(List.of());
        List<QuestionDto> questionList = questionService.findAllDeuQuestions(pageable);

        Assertions.assertEquals(0, questionList.size());
        verify(repository, times(1)).findByLanguage(Language.deu, pageable);
        verify(questionMapper, never()).toDto(any(Question.class));
    }

    @Test
    @DisplayName("Update question by id")
    void updateById_WithValidId_ReturnUpdatedQuestionDto() {
        requestDto = new QuestionRequestDto()
                .setQuestion(question.getQuestion())
                .setShortAnswer(question.getShortAnswer())
                .setFullAnswer(question.getFullAnswer())
                .setLanguage(question.getLanguage());

        when(repository.findById(question.getId())).thenReturn(Optional.of(question));
        when(questionMapper.toModel(requestDto)).thenReturn(question);
        when(repository.save(question)).thenReturn(question);
        when(questionMapper.toDto(question)).thenReturn(questionDto);

        QuestionDto actual = questionService.updateById(question.getId(), requestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(question.getId(), actual.getId());
        verify(repository, times(1)).save(question);
    }

    @Test
    @DisplayName("Given incorrect id, check if updateById throws EntityNotFoundException")
    void updateById_WithInvalidId_ThrowsEntityNotFoundException() {
        Long invalidQuestionId = 100L;
        requestDto = new QuestionRequestDto()
                .setQuestion(question.getQuestion())
                .setShortAnswer(question.getShortAnswer())
                .setFullAnswer(question.getFullAnswer())
                .setLanguage(question.getLanguage());

        when(repository.findById(invalidQuestionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> questionService.updateById(invalidQuestionId, requestDto)
        );

        String expected = "Can`t find question by id " + invalidQuestionId;
        String actual = exception.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Given correct id, check if question is deleted")
    void deleteById_WithValidQuestionId_VerifyDeletion() {
        questionService.deleteById(question.getId());

        verify(repository, times(1)).deleteById(question.getId());
    }
}
