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
public class SectorDescription {

    @Column(nullable = false)
    private String description;

    SectorDescription(String description) {
        validation(description);
        this.description = description.trim()
            .replaceAll(" +", " ");
    }

    private void validation(String description) {
        if (Objects.isNull(description) || isInvalid(description.trim().replaceAll(" +", " "))) {
            throw new UserInputException("SectorDescription이 유효하지 않습니다.");
        }
    }

    private boolean isInvalid(String description) {
        return description.isEmpty()
            || description.length() > 100;
    }
}
