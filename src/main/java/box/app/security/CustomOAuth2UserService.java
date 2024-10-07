package box.app.security;

import box.app.exception.EntityNotFoundException;
import box.app.model.user.Role;
import box.app.model.user.RoleName;
import box.app.model.user.User;
import box.app.repository.RoleRepository;
import box.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");

        if (!userRepository.existsByEmail(email)) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(oauth2User.getAttribute("given_name"));
            newUser.setLastName(oauth2User.getAttribute("family_name"));
            newUser.setPassword(passwordEncoder.encode(PasswordGenerator.generateRandomPassword()));

            Role role = roleRepository.findByName(RoleName.CUSTOMER).orElseThrow(
                    () -> new EntityNotFoundException(
                            "Can`t find role by name " + RoleName.CUSTOMER
                    )
            );
            newUser.setRole(role);

            userRepository.save(newUser);
        }

        return oauth2User;
    }
}
