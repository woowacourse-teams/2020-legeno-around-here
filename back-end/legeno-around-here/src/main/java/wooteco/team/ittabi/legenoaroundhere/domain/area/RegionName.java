package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RegionName {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 10;
    static final String NOT_ALLOWED_NULL = "이름에 null은 허용되지 않습니다.";
    static final String INVALID_LENGTH_ERROR = String
        .format("Region의 이름은 %d보다 크거나 같고, %d보다 작거나 같아야 합니다.", MIN_LENGTH, MAX_LENGTH);

    @Column(nullable = false)
    private String name;

    protected RegionName() {
    }

    public RegionName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        validateNull(name);
        validateLength(name);
    }

    private void validateNull(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException(NOT_ALLOWED_NULL);
        }
    }

    private void validateLength(String name) {
        if (name.length() < MIN_LENGTH || MAX_LENGTH < name.length()) {
            throw new IllegalArgumentException(INVALID_LENGTH_ERROR);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegionName that = (RegionName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "RegionName{" +
            "name='" + name + '\'' +
            '}';
    }
}
