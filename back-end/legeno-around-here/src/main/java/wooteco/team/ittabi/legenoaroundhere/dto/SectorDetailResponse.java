package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import java.util.stream.Collectors;
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
public class SectorDetailResponse {

    private Long id;
    private String name;
    private String description;
    private String state;
    private String reason;
    private UserResponse creator;

    public static SectorDetailResponse of(Sector sector) {
        return SectorDetailResponse.builder()
            .id(sector.getId())
            .name(sector.getStringName())
            .description(sector.getStringDescription())
            .state(sector.getStateName())
            .reason(sector.getReason())
            .creator(UserResponse.from(sector.getCreator()))
            .build();
    }

    public static List<SectorDetailResponse> listOf(List<Sector> sectors) {
        return sectors.stream()
            .map(SectorDetailResponse::of)
            .collect(Collectors.toList());
    }
}
