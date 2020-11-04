package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class SectorDetailResponse {

    private Long id;
    private String name;
    private String description;
    private String state;
    private String reason;
    private UserSimpleResponse creator;

    public static SectorDetailResponse of(Sector sector) {
        return SectorDetailResponse.builder()
            .id(sector.getId())
            .name(sector.getName())
            .description(sector.getDescription())
            .state(sector.getState())
            .reason(sector.getReason())
            .creator(UserSimpleResponse.from(sector.getCreator()))
            .build();
    }
}
