package box.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import box.app.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;
    private Role role;
    private UserDto userDto;
    private UserUpdateRequestDto updateRequestDto;
    private UserRegistrationRequestDto registrationRequestDto;

    @BeforeEach
    public void setup() {
        role = new Role();
        role.setId(1L);
        role.setName(RoleName.CUSTOMER);

        user = new User();
        user.setId(1L);
        user.setEmail("bob@example.com");
        user.setPassword("123456789encodedPassword");
        user.setFirstName("Bob");
        user.setLastName("Smith");
        user.setRole(role);

        userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
    }

    @Test
    @DisplayName("Given correct id, check if user is registered")
    void register_VerifyRegistration() throws Exception {
        //given
        registrationRequestDto = new UserRegistrationRequestDto();
        registrationRequestDto.setEmail("bob@example.com");
        registrationRequestDto.setPassword("123456789");
        registrationRequestDto.setRepeatPassword("123456789");
        registrationRequestDto.setFirstName("Bob");
        registrationRequestDto.setLastName("Smith");

        //when
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequestDto.getPassword()))
                .thenReturn("encodedPassword");

        when(roleRepository.findByName(RoleName.CUSTOMER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto actual = userService.register(registrationRequestDto);

        //then
        assertNotNull(actual);
        assertEquals(userDto, actual);
    }

    @Test
    @DisplayName("Verify user registration with existing email throws exception")
    public void register_WithExistingEmail_ThrowsRegistrationException()
            throws RegistrationException {
        //given
        registrationRequestDto = new UserRegistrationRequestDto();
        registrationRequestDto.setEmail("bob@example.com");
        registrationRequestDto.setPassword("123456789");
        registrationRequestDto.setRepeatPassword("123456789");
        registrationRequestDto.setFirstName("Bob");
        registrationRequestDto.setLastName("Smith");

        //when
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        RegistrationException thrown = assertThrows(
                RegistrationException.class,
                () -> userService.register(registrationRequestDto)
        );

        //then
        Assertions.assertEquals("Can`t register user by email: bob@example.com",
                thrown.getMessage());
    }

    @Test
    @DisplayName("Verify update user`s role by id, given incorrect id")
    void updateRoleById_WhenUserDoesNotExist_ShouldThrowException() {
        //given
        updateRequestDto = new UserUpdateRequestDto();
        updateRequestDto.setRoleName("MANAGER");

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class,
                () -> userService.updateRoleById(user.getId(), updateRequestDto));
    }

    @Test
    @DisplayName("Verify user`s info, given correct value")
    void getInfo_WhenUserExists_ShouldReturnUserDto() {
        //when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getInfo(user);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("Verify user`s info, given incorrect value")
    void getInfo_WhenUserDoesNotExist_ShouldThrowException() {
        //when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getInfo(user)
        );

        //then
        Assertions.assertEquals("Can`t find user by email:" + user.getEmail(), thrown.getMessage());
    }
}
