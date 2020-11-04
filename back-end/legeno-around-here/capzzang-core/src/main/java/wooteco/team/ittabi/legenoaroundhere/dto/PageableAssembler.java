package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableAssembler {

    private static final int MINIMUM_PAGE = 0;
    private static final int MINIMUM_SIZE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAXIMUM_SIZE = 500;
    private static final String DEFAULT_SORTED_BY = "id";

    public static Pageable assemble(PageRequest pageRequest) {
        return org.springframework.data.domain.PageRequest.of(
            calculatePage(pageRequest.getPage()),
            calculateSize(pageRequest.getSize()),
            makeDirection(pageRequest.getDirection()),
            makeSortedBy(pageRequest.getSortedBy())
        );
    }

    public static Pageable assembleForRanking(PageRequest pageRequest) {
        return org.springframework.data.domain.PageRequest.of(
            calculatePage(pageRequest.getPage()),
            calculateSize(pageRequest.getSize()));
    }

    public static int calculatePage(Integer page) {
        if (Objects.isNull(page)) {
            return MINIMUM_PAGE;
        }
        if (page < MINIMUM_PAGE) {
            throw new WrongUserInputException("Page를 잘못 [" + page + "] 입력하셨습니다.");
        }
        return page;
    }

    public static int calculateSize(Integer size) {
        if (Objects.isNull(size)) {
            return DEFAULT_SIZE;
        }
        if (size < MINIMUM_SIZE || size > MAXIMUM_SIZE) {
            throw new WrongUserInputException("Size를 잘못 [" + size + "] 입력하셨습니다.");
        }
        return size;
    }

    public static String makeSortedBy(String sortedBy) {
        if (Objects.isNull(sortedBy) || sortedBy.isEmpty()) {
            return DEFAULT_SORTED_BY;
        }
        return sortedBy;
    }

    public static Direction makeDirection(String direction) {
        if (Objects.isNull(direction) || direction.isEmpty()) {
            return Direction.ASC;
        }
        return Direction.fromOptionalString(direction)
            .orElseThrow(() -> new WrongUserInputException(
                "Direction를 잘못 [" + direction + "] 입력하셨습니다.(asc/desc)"));
    }
}
