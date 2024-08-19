package lazy.demo.auth_service.service;

import io.jsonwebtoken.Claims;
import lazy.demo.auth_service.enums.UserProvider;
import lazy.demo.auth_service.model.User;
import lazy.demo.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUserNameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }


    public User findUserByUserNameOrEmail(String username, String email) {
        return findByUserNameOrEmail(username, email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User saveOrUpdateUserOAuth(Claims claims, UserProvider provider) {
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String picture = claims.get("picture", String.class);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            // Update user info
            user.get().setFullName(name);
            user.get().setPicture(picture);
            userRepository.save(user.get());
            return user.get();
        }

        // Create new user
        return userRepository.save(User.builder()
                .email(email)
                .fullName(name)
                .picture(picture)
                .provider(provider)
                .build());
    }

    public List<User> getListUser() {
        return userRepository.findAllByOrderByUserId();
    }
}
