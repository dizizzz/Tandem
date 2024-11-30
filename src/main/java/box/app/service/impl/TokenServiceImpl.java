package box.app.service.impl;

import box.app.dto.user.UserLoginResponseDto;
import box.app.exception.EntityNotFoundException;
import box.app.mapper.UserMapper;
import box.app.model.user.User;
import box.app.repository.UserRepository;
import box.app.security.JwtUnit;
import box.app.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final RedisTokenService redisTokenService;
    private final JwtUnit jwtUnit;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserLoginResponseDto validateTempToken(String tempToken) {
        boolean exist = redisTokenService.isKeyExist(tempToken);

        if (!exist) {
            throw new EntityNotFoundException("Invalid or expired temp token");
        }

        String email = redisTokenService.getValue(tempToken);

        String token = jwtUnit.generateToken(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        redisTokenService.delete(tempToken);
        return new UserLoginResponseDto(token, userMapper.toDto(user));
    }
}
