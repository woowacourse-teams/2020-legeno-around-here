package wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(indexes = @Index(name = "idx_mailauth_email", columnList = "email"))
public class MailAuth extends BaseEntity {

    @Embedded
    private Email email;

    @Embedded
    private AuthNumber authNumber;

    @Builder
    public MailAuth(String email, int authNumber) {
        this.email = new Email(email);
        this.authNumber = new AuthNumber(authNumber);
    }

    public boolean isDifferentAuthNumber(int authNumber) {
        return this.authNumber.isDifferent(authNumber);
    }

    public void setAuthNumber(AuthNumber authNumber) {
        this.authNumber = authNumber;
    }
}
