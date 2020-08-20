package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserUpdateRequest {

    private String nickname;
    private String password;
    private Long areaId;
    private Long imageId;
}
