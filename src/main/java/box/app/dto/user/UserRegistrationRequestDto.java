package box.app.dto.user;

import box.app.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
@Data
@Accessors(chain = true)
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 250)
    private String password;

    @NotBlank
    @Length(min = 8, max = 150)
    private String repeatPassword;

    @NotBlank
    @Length(min = 2, max = 40)
    private String firstName;

    @NotBlank
    private String lastName;

}
