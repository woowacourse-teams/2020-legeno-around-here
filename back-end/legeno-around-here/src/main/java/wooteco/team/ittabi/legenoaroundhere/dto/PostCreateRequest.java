package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PostCreateRequest {

    private String writing;
    private List<MultipartFile> images;
    private Long areaId;
    private Long sectorId;
}
