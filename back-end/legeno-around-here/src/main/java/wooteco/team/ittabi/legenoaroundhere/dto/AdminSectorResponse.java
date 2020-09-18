package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
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
public class AdminSectorResponse {

    private Long id;
    private String name;
    private String description;
    private UserSimpleResponse creator;
    private LocalDateTime createdAt;
    private UserSimpleResponse lastModifier;
    private LocalDateTime lastModifiedAt;
    private String state;
    private String reason;

    public static AdminSectorResponse of(Sector sector) {
        return AdminSectorResponse.builder()
            .id(sector.getId())
            .name(sector.getName())
            .description(sector.getDescription())
            .creator(UserSimpleResponse.from(sector.getCreator()))
            .createdAt(sector.getCreatedAt())
            .lastModifier(UserSimpleResponse.from(sector.getLastModifier()))
            .lastModifiedAt(sector.getModifiedAt())
            .state(sector.getState())
            .reason(sector.getReason())
            .build();
    }
}
