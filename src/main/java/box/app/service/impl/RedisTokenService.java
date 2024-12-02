package box.app.service.impl;

import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    public String getToken(String email) {
        String tempToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(tempToken, email, Duration.ofMinutes(5));
        return tempToken;
    }

    public boolean isKeyExist(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public String getValue(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void delete(String token) {
        redisTemplate.delete(token);
    }
}
