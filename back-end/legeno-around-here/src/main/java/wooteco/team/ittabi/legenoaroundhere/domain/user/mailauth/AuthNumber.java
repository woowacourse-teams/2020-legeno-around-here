package wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class AuthNumber {

    @Column(nullable = false)
    private Integer authNumber;

    public boolean isDifferent(Integer authNumber) {
        return !this.authNumber.equals(authNumber);
    }
}

