package box.app.service.impl;

import box.app.dto.question.QuestionDto;
import box.app.dto.question.QuestionRequestDto;
import box.app.exception.EntityNotFoundException;
import box.app.mapper.QuestionMapper;
import box.app.model.question.Question;
import box.app.repository.QuestionRepository;
import box.app.service.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository repository;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionDto add(QuestionRequestDto requestDto) {
        Question question = questionMapper.toModel(requestDto);
        return questionMapper.toDto(repository.save(question));
    }

    @Override
    public List<QuestionDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).stream()
                .map(questionMapper::toDto)
                .toList();
    }

    @Override
    public QuestionDto getById(Long id) {
        Question question = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find question by id " + id));
        return questionMapper.toDto(question);
    }

    @Override
    public QuestionDto updateById(Long id, QuestionRequestDto requestDto) {
        Question question = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find question by id " + id));
        Question updatedQuestion = questionMapper.toModel(requestDto);
        updatedQuestion.setId(id);
        return questionMapper.toDto(repository.save(updatedQuestion));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
