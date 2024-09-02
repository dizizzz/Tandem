package box.app.service;

import box.app.dto.user.UserDto;
import box.app.dto.user.UserRegistrationRequestDto;
import box.app.dto.user.UserUpdateRequestDto;
import box.app.exception.RegistrationException;
import box.app.model.user.User;

public interface UserService {
    UserDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserDto updateRoleById(Long id, UserUpdateRequestDto requestDto);

    UserDto getInfo(User authUser);

    UserDto updateInfo(User updateUser, UserRegistrationRequestDto requestDto);
}
