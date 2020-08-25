package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAssembler {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static User assemble(UserCreateRequest userCreateRequest, Area area) {
        return User.builder()
            .email(userCreateRequest.getEmail())
            .nickname(userCreateRequest.getNickname())
            .password(PASSWORD_ENCODER.encode(userCreateRequest.getPassword()))
            .area(area)
            .build();
    }
}
