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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
            .name(sector.getName())
            .description(sector.getDescription())
            .creator(UserResponse.from(sector.getCreator()))
            .build();
    }

    public static List<SectorResponse> listOf(List<Sector> sector) {
        return sector.stream()
            .map(SectorResponse::of)
            .collect(Collectors.toList());
    }
}
