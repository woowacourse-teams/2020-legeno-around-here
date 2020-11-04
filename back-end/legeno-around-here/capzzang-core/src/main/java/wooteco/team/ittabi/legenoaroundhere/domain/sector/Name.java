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
public class Name {

    @Column(nullable = false)
    private String name;

    private Name(String name) {
        this.name = name.toUpperCase();
    }

    public static Name of(String name) {
        validate(name);
        return new Name(name.trim()
            .replaceAll(" +", " "));
    }

    public static Name getKeywordName(String keyword) {
        return new Name(keyword);
    }

    private static void validate(String name) {
        if (Objects.isNull(name) || isInvalid(name.trim().replaceAll("  +", " "))) {
            throw new WrongUserInputException("부문명을 입력해주세요!");
        }
    }

    private static boolean isInvalid(String name) {
        return name.isEmpty() || name.length() > 20;
    }
}
