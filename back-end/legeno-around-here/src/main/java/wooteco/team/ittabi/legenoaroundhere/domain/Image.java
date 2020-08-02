package wooteco.team.ittabi.legenoaroundhere.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "image")
@Getter
@Setter
@ToString(exclude = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    private String name;
    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Image(String name, String url) {
        this.name = name;
        this.url = url;
    }

}
