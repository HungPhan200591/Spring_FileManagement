package lazy.demo.auth_service.controller;

import lazy.demo.auth_service.dto.resp.GenericResponse;
import lazy.demo.auth_service.dto.resp.UserDetailResp;
import lazy.demo.auth_service.model.User;
import lazy.demo.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getListUser() {
        return userService.getListUser();
    }

    @GetMapping("/profile")
    public ResponseEntity<GenericResponse<UserDetailResp>> getUserProfile(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(GenericResponse.success(userService.getUserProfile(token)));
    }

}
