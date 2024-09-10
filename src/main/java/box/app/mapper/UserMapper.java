package box.app.mapper;

import box.app.config.MapperConfig;
import box.app.dto.user.UserDto;
import box.app.dto.user.UserRegistrationRequestDto;
import box.app.model.user.Role;
import box.app.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
    UserDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    @Named("mapRoleToString")
    default String mapRoleToString(Role role) {
        return role != null ? role.getName().name() : null;
    }
}
