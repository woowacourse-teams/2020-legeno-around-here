package wooteco.team.ittabi.legenoaroundhere.dto;

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
public class NotificationResponse {

    private Long id;
    private String content;
    private String location;
    private Boolean isRead;

    public static NotificationResponse of(Long id, String content, String location,
        Boolean isRead) {
        return NotificationResponse.builder()
            .id(id)
            .content(content)
            .location(location)
            .isRead(isRead)
            .build();
    }

    public void setRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
