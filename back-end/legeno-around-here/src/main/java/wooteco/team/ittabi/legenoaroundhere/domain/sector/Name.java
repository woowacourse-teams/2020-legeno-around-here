package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Name {

    @Column(unique = true, nullable = false)
    private String name;

    Name(String name) {
        validate(name);
        this.name = name.trim()
            .replaceAll(" +", " ")
            .toUpperCase();
    }

    private void validate(String name) {
        if (Objects.isNull(name) || isInvalid(name.trim().replaceAll("  +", " "))) {
            throw new UserInputException("SectorName이 유효하지 않습니다.");
        }
    }

    private boolean isInvalid(String name) {
        return name.isEmpty() || name.length() > 20;
    }
}
