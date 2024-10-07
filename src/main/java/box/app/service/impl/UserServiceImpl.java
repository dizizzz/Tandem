package box.app.service.impl;

import box.app.dto.user.UserDto;
import box.app.dto.user.UserRegistrationRequestDto;
import box.app.dto.user.UserUpdateRequestDto;
import box.app.exception.EntityNotFoundException;
import box.app.exception.RegistrationException;
import box.app.mapper.UserMapper;
import box.app.model.user.Role;
import box.app.model.user.RoleName;
import box.app.model.user.User;
import box.app.repository.RoleRepository;
import box.app.repository.UserRepository;
import box.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        String email = requestDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new RegistrationException("Can`t register user by email: " + email);
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setLastName(requestDto.getLastName());
        user.setFirstName(requestDto.getFirstName());
        Role role = roleRepository.findByName(RoleName.CUSTOMER).orElseThrow(
                () -> new EntityNotFoundException("Can`t find role by name" + RoleName.CUSTOMER)
        );
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto updateRoleById(Long id, UserUpdateRequestDto requestDto) {
        String roleName = requestDto.getRoleName();
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user by id:" + id)
        );
        Role newRole = roleRepository.findByName(RoleName.valueOf(roleName)).orElseThrow(
                () -> new EntityNotFoundException("Can`t find role by name" + roleName)
        );
        user.setRole(newRole);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getInfo(User authUser) {
        User user = userRepository.findByEmail(authUser.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user by email:" + authUser.getEmail())
        );
        return userMapper.toDto(user);
    }
}
