package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserCreateRequest userCreateRequest) {
        Long userId = userService.createUser(userCreateRequest);
        return ResponseEntity
            .created(URI.create("/users/" + userId))
            .build();
    }

    @CrossOrigin
    @PostMapping("/joinAdmin")
    public ResponseEntity<Void> joinAdmin(@RequestBody UserCreateRequest userCreateRequest) {
        Long userId = userService.createAdmin(userCreateRequest);
        return ResponseEntity
            .created(URI.create("/users/" + userId))
            .build();
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @CrossOrigin
    @GetMapping("/users/myinfo")
    public ResponseEntity<UserResponse> findUser(Authentication authentication) {
        return ResponseEntity.ok(userService.findUser(authentication));
    }
}
