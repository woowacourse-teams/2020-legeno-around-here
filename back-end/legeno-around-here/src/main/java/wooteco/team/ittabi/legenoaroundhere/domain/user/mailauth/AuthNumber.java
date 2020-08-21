package wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode
public class AuthNumber {

    private int AuthNumber;

    public AuthNumber(int authNumber) {
        AuthNumber = authNumber;
    }
}
