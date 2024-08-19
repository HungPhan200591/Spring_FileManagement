package lazy.demo.auth_service.service;

import lazy.demo.auth_service.model.User;
import lazy.demo.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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
}
