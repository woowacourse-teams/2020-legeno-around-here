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
public class AdminSectorResponse {

    private Long id;
    private String name;
    private String description;
    private UserResponse creator;
    private UserResponse lastModifier;
    private String state;
    private String reason;

    public static AdminSectorResponse of(Sector sector) {
        return AdminSectorResponse.builder()
            .id(sector.getId())
            .name(sector.getStringName())
            .description(sector.getStringDescription())
            .creator(UserResponse.from(sector.getCreator()))
            .lastModifier(UserResponse.from(sector.getLastModifier()))
            .state(sector.getStateName())
            .reason(sector.getReason())
            .build();
    }

    public static List<AdminSectorResponse> listOf(List<Sector> sectors) {
        return sectors.stream()
            .map(AdminSectorResponse::of)
            .collect(Collectors.toList());
    }
}
