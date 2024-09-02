package box.app.dto.question;

import box.app.model.question.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class QuestionDto {
    private Long id;
    private String question;
    private String shortAnswer;
    private String fullAnswer;
    private Language language;
}
