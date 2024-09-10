package box.app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class UserLoginResponseDto {
    private String token;
    private UserDto userDto;
}
