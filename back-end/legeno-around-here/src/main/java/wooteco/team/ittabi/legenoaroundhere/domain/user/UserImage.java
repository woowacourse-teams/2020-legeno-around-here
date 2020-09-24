package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAvailableException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "user")
@SQLDelete(sql = "UPDATE user_image SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class UserImage extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserImage(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public boolean hasNotUser() {
        return Objects.isNull(this.user);
    }

    public void setUser(User user) {
        if (Objects.isNull(this.user)) {
            this.user = user;
            return;
        }
        throw new NotAvailableException("해당 사용자를 셋팅할 수 없습니다.");
    }
}
