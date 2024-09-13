package box.app.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageDto {
    private Long id;
    private String name;
    private String email;
    private String message;
}
