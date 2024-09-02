package box.app.mapper;

import box.app.config.MapperConfig;
import box.app.dto.user.UserDto;
import box.app.dto.user.UserRegistrationRequestDto;
import box.app.model.user.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
