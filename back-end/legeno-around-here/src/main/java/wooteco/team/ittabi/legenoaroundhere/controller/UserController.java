package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ME_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.USERS_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.USERS_PATH_WITH_SLASH;

import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserImageResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserOtherResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserPasswordUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.service.UserService;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserCreateRequest userCreateRequest) {
        Long userId = userService.createUser(userCreateRequest);

        return ResponseEntity
            .created(URI.create(USERS_PATH_WITH_SLASH + userId))
            .build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse token = userService.login(loginRequest);

        return ResponseEntity
            .ok(token);
    }

    @GetMapping(USERS_PATH + ME_PATH)
    public ResponseEntity<UserResponse> findMe() {
        UserResponse user = userService.findMe();

        return ResponseEntity
            .ok(user);
    }

    @GetMapping(USERS_PATH_WITH_SLASH + "{userId}")
    public ResponseEntity<UserOtherResponse> findUser(@PathVariable Long userId) {
        UserOtherResponse user = userService.findUser(userId);

        return ResponseEntity
            .ok(user);
    }

    @PostMapping("/users/images")
    public ResponseEntity<UserImageResponse> uploadUserImage(MultipartFile image) {
        UserImageResponse userImage = userService.uploadUserImage(image);

        return ResponseEntity
            .created(URI.create("/user-images/" + userImage.getId()))
            .body(userImage);
    }

    @PutMapping(USERS_PATH + ME_PATH)
    public ResponseEntity<UserResponse> updateMe(@RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse user = userService.updateMe(userUpdateRequest);

        return ResponseEntity
            .ok(user);
    }

    @DeleteMapping(USERS_PATH + ME_PATH)
    public ResponseEntity<Void> deleteMe() {
        userService.deleteMe();

        return ResponseEntity
            .noContent()
            .build();
    }

    @PutMapping(USERS_PATH + ME_PATH + "/password")
    public ResponseEntity<Void> changeMyPassword(
        @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        userService.changeMyPassword(userPasswordUpdateRequest);

        return ResponseEntity
            .noContent()
            .build();
    }
}
