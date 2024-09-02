package box.app.dto.question;

import box.app.model.question.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuestionRequestDto {
    @NotBlank
    private String question;
    @NotBlank
    private String shortAnswer;
    @NotBlank
    private String fullAnswer;
    @NotNull
    private Language language;
}
