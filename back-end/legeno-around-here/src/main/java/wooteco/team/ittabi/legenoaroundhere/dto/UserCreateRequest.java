package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class UserCreateRequest {

    private String email;
    private String nickname;
    private String password;
    private Long areaId;
    private Integer authNumber;

    public MailAuthCheckRequest toMailAuthCheckRequest() {
        return new MailAuthCheckRequest(email, authNumber);
    }
}
