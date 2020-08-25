package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Description {

    @Column(nullable = false)
    private String description;

    Description(String description) {
        validate(description);
        this.description = description.trim()
            .replaceAll(" +", " ");
    }

    private void validate(String description) {
        if (Objects.isNull(description) || isInvalid(description.trim().replaceAll(" +", " "))) {
            throw new WrongUserInputException("SectorDescription이 유효하지 않습니다.");
        }
    }

    private boolean isInvalid(String description) {
        return description.isEmpty()
            || description.length() > 100;
    }

    public String getDescription(int wordCount) {
        return description.substring(0, wordCount);
    }
}
