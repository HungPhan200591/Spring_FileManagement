package lazy.demo.auth_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lazy.demo.auth_service.dto.resp.LoginResp;
import lazy.demo.auth_service.config.security.jwt.JwtService;
import lazy.demo.auth_service.dto.req.LoginReq;
import lazy.demo.auth_service.dto.req.RegisterReq;
import lazy.demo.auth_service.dto.req.RefreshTokenReq;
import lazy.demo.auth_service.dto.resp.GenericResponse;
import lazy.demo.auth_service.model.Token;
import lazy.demo.auth_service.model.User;
import lazy.demo.auth_service.service.TokenService;
import lazy.demo.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<LoginResp>> login(@RequestBody LoginReq loginReq, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        System.out.println("IP Address: " + ipAddress);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String authenticatedUsername = authentication.getName();
            Token token = tokenService.saveToken(authenticatedUsername, loginReq.getDeviceInfo(), ipAddress);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(GenericResponse.success(new LoginResp(token.getToken(), token.getRefreshToken())));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<User>>register(@RequestBody RegisterReq registerReq) {
        User newUser = userService.registerUser(modelMapper.map(registerReq, User.class));
        return ResponseEntity.ok(GenericResponse.success(newUser));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<GenericResponse<LoginResp>> refreshToken(@RequestBody RefreshTokenReq request) {
        LoginResp newToken = tokenService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(GenericResponse.success(newToken));
    }

    @PostMapping("/revoke")
    public ResponseEntity<GenericResponse<?>> revokeToken(@RequestHeader("Authorization") String token) {
        tokenService.revokeToken(token);
        return ResponseEntity.ok(GenericResponse.success("Token has been revoked"));
    }
}