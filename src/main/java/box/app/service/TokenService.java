package box.app.service;

import box.app.dto.user.UserLoginResponseDto;

public interface TokenService {
    UserLoginResponseDto validateTempToken(String tempToken);
}
