package box.app.dto.message;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageRequestDto {
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    private String message;
}
