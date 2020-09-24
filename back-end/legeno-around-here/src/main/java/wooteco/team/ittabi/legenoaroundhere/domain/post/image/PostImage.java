package wooteco.team.ittabi.legenoaroundhere.domain.post.image;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(exclude = "post")
@SQLDelete(sql = "UPDATE post_image SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class PostImage extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostImage(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public boolean hasPost() {
        return Objects.nonNull(this.post);
    }

    public boolean isContainsOf(List<Long> deletePostImageIds) {
        return deletePostImageIds.contains(this.getId());
    }

    public void setPost(Post post) {
        if (Objects.nonNull(this.post)) {
            this.post.getPostImages().remove(this);
        }
        this.post = post;
    }
}
