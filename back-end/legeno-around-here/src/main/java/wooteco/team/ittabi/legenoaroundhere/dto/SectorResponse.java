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
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SectorResponse {

    private Long id;
    private String name;
    private String description;
    private UserResponse creator;

    public static SectorResponse of(Sector sector) {
        return SectorResponse.builder()
            .id(sector.getId())
            .name(sector.getStringName())
            .description(sector.getStringDescription())
            .creator(UserResponse.from(sector.getCreator()))
            .build();
    }
}
