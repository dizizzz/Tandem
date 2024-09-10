package box.app.security;

import box.app.dto.user.UserLoginRequestDto;
import box.app.dto.user.UserLoginResponseDto;
import box.app.exception.EntityNotFoundException;
import box.app.mapper.UserMapper;
import box.app.model.user.User;
import box.app.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUnit jwtUnit;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TokenBlacklistService tokenBlacklistService;

    public UserLoginResponseDto authentication(UserLoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find user by email:" + requestDto.email()));
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.email(), requestDto.password()
                )
        );
        String token = jwtUnit.generateToken(authentication.getName());
        return new UserLoginResponseDto(token, userMapper.toDto(user));
    }

    public UserLoginResponseDto refreshToken(String authorizationHeader) {
        String oldToken = extractToken(authorizationHeader);
        if (jwtUnit.isValidToken(oldToken)) {
            String username = jwtUnit.getUsername(oldToken);

            String newToken = jwtUnit.generateToken(username);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
            return new UserLoginResponseDto(newToken, userMapper.toDto(user));
        } else {
            throw new JwtException("Invalid or expired token");
        }
    }

    public void logout(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (jwtUnit.isValidToken(token)) {
            tokenBlacklistService.addTokenToBlacklist(token);
        } else {
            throw new JwtException("Invalid token");
        }
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new JwtException("Invalid token format");
        }
    }
}
