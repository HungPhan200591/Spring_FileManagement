package lazy.demo.auth_service.controller;

import lazy.demo.auth_service.config.security.jwt.JwtResponse;
import lazy.demo.auth_service.config.security.jwt.JwtService;
import lazy.demo.auth_service.dto.req.LoginReq;
import lazy.demo.auth_service.dto.req.RegisterReq;
import lazy.demo.auth_service.dto.req.TokenRefreshReq;
import lazy.demo.auth_service.dto.resp.GenericResponse;
import lazy.demo.auth_service.model.User;
import lazy.demo.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<JwtResponse>> login(@RequestBody LoginReq loginReq) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication.getName());
        String refreshToken = jwtService.generateRefreshToken(authentication.getName());
        return ResponseEntity.ok(GenericResponse.success(new JwtResponse(token, refreshToken)));
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<User>>register(@RequestBody RegisterReq registerReq) {
        System.out.println("registerReq: " + registerReq);
        User newUser = userService.registerUser(modelMapper.map(registerReq, User.class));
        return ResponseEntity.ok(GenericResponse.success(newUser));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshReq request) {
        String newAccessToken = jwtService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(newAccessToken);
    }
}