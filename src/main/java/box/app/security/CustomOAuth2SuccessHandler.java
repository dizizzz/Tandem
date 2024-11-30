package box.app.security;

import box.app.exception.EntityNotFoundException;
import box.app.model.user.RoleName;
import box.app.model.user.User;
import box.app.repository.UserRepository;
import box.app.service.impl.RedisTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUnit jwtUnit;
    private final UserRepository userRepository;
    private final RedisTokenService redisTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find user by email " + email
                )
        );

        String tempToken = redisTokenService.getToken(email);

        String redirectUrl = determineRedirectUrl(user, tempToken);
        response.sendRedirect(redirectUrl);
    }

    private String determineRedirectUrl(User user, String tempToken) {
        if (user.getRole().getName().equals(RoleName.ADMIN)) {
            return "https://tandem.netlify.app/#/admin?token=" + tempToken;
        }
        return "https://tandem.netlify.app/#/profile?token=" + tempToken;
    }
}
