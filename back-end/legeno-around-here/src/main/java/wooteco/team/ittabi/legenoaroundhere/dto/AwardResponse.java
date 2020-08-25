package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class AwardResponse {

    private String name;
    private String period;
    private String location;

    public static AwardResponse of(String name, String period, String location) {
        return AwardResponse.builder()
            .name(name)
            .period(period)
            .location(location)
            .build();
    }
}
