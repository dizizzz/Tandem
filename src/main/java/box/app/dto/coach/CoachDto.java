package box.app.dto.coach;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CoachDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String coachTitle;
    private String description;
    private String photo;
}
