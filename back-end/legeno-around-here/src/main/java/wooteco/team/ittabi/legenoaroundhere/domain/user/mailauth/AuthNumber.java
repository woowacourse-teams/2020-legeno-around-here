package wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode
public class AuthNumber {

    private Integer authNumber;

    public boolean isSame(Integer authNumber) {
        return this.authNumber.equals(authNumber);
    }
}

