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
public class SectorSimpleResponse {

    private Long id;
    private String name;

    public static SectorSimpleResponse of(Sector sector) {
        return SectorSimpleResponse.builder()
            .id(sector.getId())
            .name(sector.getName())
            .build();
    }

    public static List<SectorSimpleResponse> listOf(List<Sector> sector) {
        return sector.stream()
            .map(SectorSimpleResponse::of)
            .collect(Collectors.toList());
    }
}
