package box.app.mapper;

import box.app.config.MapperConfig;
import box.app.dto.question.QuestionDto;
import box.app.dto.question.QuestionRequestDto;
import box.app.model.question.Question;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface QuestionMapper {
    QuestionDto toDto(Question question);

    Question toModel(QuestionRequestDto requestDto);
}
