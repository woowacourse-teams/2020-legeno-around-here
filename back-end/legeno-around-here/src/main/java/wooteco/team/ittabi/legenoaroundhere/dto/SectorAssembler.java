package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class SectorAssembler {

    public static Sector assemble(User user, SectorRequest sectorRequest, SectorState state) {
        return Sector.builder()
            .name(sectorRequest.getName())
            .description(sectorRequest.getDescription())
            .creator(user)
            .lastModifier(user)
            .state(state)
            .build();
    }
}
