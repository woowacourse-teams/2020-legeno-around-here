package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Objects;

public class RegionName {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 10;
    static final String INVALID_LENGTH_ERROR = String
        .format("Region의 이름은 %d보다 크거나 같고, %d보다 작거나 같아야 합니다.", MIN_LENGTH, MAX_LENGTH);

    private final String name;

    public RegionName(String name) {
        validateLength(name);
        this.name = name;
    }

    private void validateLength(String name) {
        if (name.length() < MIN_LENGTH || MAX_LENGTH < name.length()) {
            throw new IllegalArgumentException(
                INVALID_LENGTH_ERROR);
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
}
