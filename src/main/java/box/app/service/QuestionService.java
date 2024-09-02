package box.app.service;

import box.app.dto.question.QuestionDto;
import box.app.dto.question.QuestionRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionService {
    QuestionDto add(QuestionRequestDto requestDto);

    List<QuestionDto> findAll(Pageable pageable);

    QuestionDto getById(Long id);

    QuestionDto updateById(Long id, QuestionRequestDto requestDto);

    void deleteById(Long id);
}
