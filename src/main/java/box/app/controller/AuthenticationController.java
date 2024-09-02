package box.app.controller;

import box.app.dto.user.UserDto;
import box.app.dto.user.UserLoginRequestDto;
import box.app.dto.user.UserLoginResponseDto;
import box.app.dto.user.UserRegistrationRequestDto;
import box.app.exception.RegistrationException;
import box.app.security.AuthenticationService;
import box.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management", description = "Endpoints for managing authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate the user", description = "Authenticate the user")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authentication(requestDto);
    }

    @PostMapping("/registration")
    @Operation(summary = "Register the user", description = "Register the user")
    public UserDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
