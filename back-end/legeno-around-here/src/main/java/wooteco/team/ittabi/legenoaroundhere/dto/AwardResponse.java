package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
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
    private String description;
    private LocalDateTime date;
    private String location;

    public static AwardResponse of(String name, String description, LocalDateTime date,
        String location) {
        return AwardResponse.builder()
            .name(name)
            .description(description)
            .date(date)
            .location(location)
            .build();
    }
}
