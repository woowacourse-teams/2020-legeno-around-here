package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AreaResponse {

    private String fullName;
    private String firstDepthName;
    private String secondDepthName;
    private String thirdDepthName;
    private String fourthDepthName;

    public static AreaResponse of(Area area) {
        if (Objects.isNull(area)) {
            return null;
        }
        return AreaResponse.builder()
            .fullName(area.getFullName())
            .firstDepthName(area.getFirstDepthName())
            .secondDepthName(area.getSecondDepthName())
            .thirdDepthName(area.getThirdDepthName())
            .fourthDepthName(area.getFourthDepthName())
            .build();
    }
}
