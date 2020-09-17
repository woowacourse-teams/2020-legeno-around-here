package wooteco.team.ittabi.legenoaroundhere.adminController;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ADMIN_PATH;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.service.UserService;

@RestController
@RequestMapping(ADMIN_PATH)
@AllArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginAdmin(@RequestBody LoginRequest loginRequest) {
        TokenResponse token = userService.loginAdmin(loginRequest);

        return ResponseEntity
            .ok(token);
    }
}
