package box.app.controller;

import box.app.dto.user.UserLoginResponseDto;
import box.app.service.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://tandem.netlify.app")
@Tag(name = "Token management", description = "Endpoints for managing token")
@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/validate")
    public UserLoginResponseDto validateToken(@RequestParam String tempToken) {
        return tokenService.validateTempToken(tempToken);
    }
}
