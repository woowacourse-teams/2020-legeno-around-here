package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserRequest userRequest) {
        Long userId = userService.createUser(userRequest);
        return ResponseEntity
            .created(URI.create("/users/" + userId))
            .build();
    }

    @PostMapping("/joinAdmin")
    public ResponseEntity<Void> joinAdmin(@RequestBody UserRequest userRequest) {
        Long userId = userService.createAdmin(userRequest);
        return ResponseEntity
            .created(URI.create("/users/" + userId))
            .build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @GetMapping("/users/myinfo")
    public ResponseEntity<UserResponse> findUser() {
        return ResponseEntity.ok(userService.findUser());
    }

    @CrossOrigin
    @PutMapping("/users/myinfo")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(userRequest));
    }

    @CrossOrigin
    @DeleteMapping("/users/myinfo")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity
            .noContent()
            .build();
    }
}
