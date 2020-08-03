package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SectorRequest {

    private String name;
    private String description;

    public Sector toSector(User user) {
        return new Sector(name, description, user);
    }
}
