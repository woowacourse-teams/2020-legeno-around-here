package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@AllArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class PageRequest {

    protected static final int MINIMUM_PAGE = 1;
    protected static final int INDEX_CORRECTION = 1;
    protected static final int MINIMUM_SIZE = 1;
    protected static final int DEFAULT_SIZE = 10;
    protected static final int MAXIMUM_SIZE = 50;
    protected static final String DEFAULT_SORTED_BY = "id";

    private Integer page;
    private Integer size;
    private String sortedBy;
    private String direction;

    public Pageable getPageable() {
        return org.springframework.data.domain.PageRequest
            .of(calculatePage(), calculateSize(), makeDirection(), makeSortedBy());
    }

    public int calculatePage() {
        if (Objects.isNull(page)) {
            return MINIMUM_PAGE - INDEX_CORRECTION;
        }
        if (page < MINIMUM_PAGE) {
            throw new WrongUserInputException("Page를 잘못 [" + page + "] 입력하셨습니다.");
        }
        return page - INDEX_CORRECTION;
    }

    public int calculateSize() {
        if (Objects.isNull(size)) {
            return DEFAULT_SIZE;
        }
        if (size < MINIMUM_SIZE || size > MAXIMUM_SIZE) {
            throw new WrongUserInputException("Page를 잘못 [" + size + "] 입력하셨습니다.");
        }
        return size;
    }

    public String makeSortedBy() {
        if (Objects.isNull(sortedBy) || sortedBy.isEmpty()) {
            return DEFAULT_SORTED_BY;
        }
        return sortedBy;
    }

    public Direction makeDirection() {
        if (Objects.isNull(direction) || direction.isEmpty()) {
            return Direction.ASC;
        }
        return Direction.fromOptionalString(direction)
            .orElseThrow(() -> new WrongUserInputException(
                "Direction를 잘못 [" + direction + "] 입력하셨습니다.(asc/desc)"));
    }
}
