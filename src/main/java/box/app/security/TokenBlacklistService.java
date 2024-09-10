package box.app.security;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void addTokenToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
